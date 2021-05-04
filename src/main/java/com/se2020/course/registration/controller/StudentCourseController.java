package com.se2020.course.registration.controller;

import com.se2020.course.registration.entity.*;
import com.se2020.course.registration.repository.*;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.se2020.course.registration.utils.DateTimeUtils.parseDate;
import static com.se2020.course.registration.utils.SecurityUtils.hashPassword;

@RestController
public class StudentCourseController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final long DURATION = 14*24*60*60*1000;

    @PutMapping("/register/course/{id}")
    public String register( @RequestBody User auth,
                            @PathVariable(name = "id") Long courseId){
        Permission p = permissionRepository.findByPermission("register_my_course").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            Course c = courseRepository.findById(courseId).orElse(null);
            if (c!= null){
                final String hashedPass = hashPassword(auth.getPassword());
                User u = userRepository.findByEmailAndPasswordAndRole(auth.getEmail(), hashedPass, "student").get(0);
                Student stu = studentRepository.findById(u.getStudentId()).get();
                // capacity check
                if (c.getCurrentStudents().size() >= c.getCapacity()){
                    return "Full capacity";
                }

                // prerequisite check
                List<String> missingPre = missingPrerequisites(c, stu);
                if (missingPre.size() >0 ){
                    return "Missing prerequisites: " + missingPre.toString();
                }

                stu.addCourse(c);
                studentRepository.save(stu);
                return "Success";
            }
            return "Course not found";
        }
        return "Access denied";
    }

    @PutMapping("/register/course/{courseId}/{studentId}")
    public String register(@RequestBody User auth,
                           @PathVariable(name = "courseId") Long courseId,
                           @PathVariable(name = "studentId") Long studentId){
        Permission p = permissionRepository.findByPermission("register_course").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            Course c = courseRepository.findById(courseId).orElse(null);
            if (c != null){
                Student stu = studentRepository.findById(studentId).get();
                stu.addCourse(c);
                studentRepository.save(stu);
                return "Success";
            }
            return "Course not found";
        }
        return "Access denied";
    }

    @PutMapping("/cancel/course/{id}")
    public String cancel(@RequestBody User auth,
                         @PathVariable(name = "id") Long courseId){
        Permission p = permissionRepository.findByPermission("cancel_my_course").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            Course c = courseRepository.findById(courseId).orElse(null);
            if (c != null){
                Instant now = new Instant();
                Instant startDT = parseDate(c.getStartDate());
                Instant cancelDdl = startDT.plus(DURATION);
                if (now.isAfter(cancelDdl)){
                    return "Beyond cancellation deadline";
                }
                final String hashedPass = hashPassword(auth.getPassword());
                User u = userRepository.findByEmailAndPasswordAndRole(auth.getEmail(), hashedPass, "student").get(0);
                Student stu = studentRepository.findById(u.getStudentId()).get();
                stu.removeCourse(c);
                studentRepository.save(stu);
                return "Success";
            }
            return "Course not found";
        }
        return "Access denied";
    }

    @PutMapping("cancel/course/{courseId}/{studentId}")
    public String cancel(@RequestBody User auth,
                         @PathVariable(name = "courseId") Long courseId,
                         @PathVariable(name = "studentId") Long studentId){
        Permission p = permissionRepository.findByPermission("cancel_course").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            Course c = courseRepository.findById(courseId).orElse(null);
            if (c != null){
                Student stu = studentRepository.findById(studentId).get();
                stu.removeCourse(c);
                studentRepository.save(stu);
                return "Success";
            }
            return "Course not found";
        }
        return "Access denied";
    }

    private boolean hasPermission(User auth, Permission p){
        final String hashedPass = hashPassword(auth.getPassword());
        List<User> auths = userRepository.findByEmailAndPassword(auth.getEmail(), hashedPass);
        if (auths.size() > 0){
            for (User u: auths){
                Role role = roleRepository.findByRole(u.getRole()).get(0);
                if (role.getPermissions().contains(p)){
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> missingPrerequisites(Course c, Student student){
        List<String> missingPre = new ArrayList<>();
        Set<Course> pre = c.getPrerequisites();
        Set<Course> pastCourses = student.getPastCourses();

        for (Course p: pre){
            if (!pastCourses.contains(p)){
                missingPre.add(p.getName());
            }
        }
        return missingPre;
    }

}

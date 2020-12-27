package com.se2020.course.registration.controller;

import com.se2020.course.registration.CourseRegistrationApplication;
import com.se2020.course.registration.entity.*;
import com.se2020.course.registration.repository.CourseRepository;
import com.se2020.course.registration.repository.PermissionRepository;
import com.se2020.course.registration.repository.RoleRepository;
import com.se2020.course.registration.repository.UserRepository;
import com.se2020.course.registration.wrapper.AuthCourseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se2020.course.registration.utils.SecurityUtils.hashPassword;

@RestController
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("/course")
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    @GetMapping("/course/{id}")
    public Course getCourse(@PathVariable(name = "id") Long id){
        return courseRepository.findById(id).orElse(null);
    }

    @PostMapping("/course")
    public String addCourse(@RequestBody AuthCourseWrapper authCourseWrapper){
        User auth = authCourseWrapper.getAuth();
        Permission p = permissionRepository.findByPermission("edit_course").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            Course c = authCourseWrapper.getCourse();
            if (c.getCode() != null){
                List<Course> courses = courseRepository.findByCode(c.getCode().toUpperCase());
                if (courses.size() > 0){
                    return "Course exists";
                }
                courseRepository.save(c);
                return "Success";
            }
            return "Missing course code";
        }
        return "Access denied";

    }

    @PutMapping("/course/{id}")
    public Course updateCourse(@RequestBody AuthCourseWrapper authCourseWrapper,
                               @PathVariable(name = "id") Long id){
        User auth = authCourseWrapper.getAuth();
        Permission p = permissionRepository.findByPermission("edit_course").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            Course c = courseRepository.findById(id).orElse(null);
            if (c != null){
                Course newCourse = authCourseWrapper.getCourse();
                c.setName(newCourse.getName());
                c.setCode(newCourse.getCode());
                c.setCapacity(newCourse.getCapacity());
                c.setStartDate(newCourse.getStartDate());
                c.setEndDate(newCourse.getEndDate());
                setCoursePrerequisitesHelper(c);
                return courseRepository.save(c);
            }
        }
        return null;
    }

    @DeleteMapping("/course/{id}")
    public void deleteCourse(@RequestBody User auth,
                             @PathVariable(name = "id") Long id){
        Permission p = permissionRepository.findByPermission("edit_course").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            Course c = courseRepository.findById(id).orElse(null);
            if (c != null){
                courseRepository.delete(c);
            }
        }
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

    private void setCoursePrerequisitesHelper(Course course){
        for (Course pre: course.getPrerequisites()){
            Course p = courseRepository.findById(pre.getId()).orElse(null);
            if (p == null){
                courseRepository.save(pre);
                course.addPrerequisite(pre);
            } else {
                course.addPrerequisite(p);
            }

        }
    }
}

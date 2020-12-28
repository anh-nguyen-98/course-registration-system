package com.se2020.course.registration.controller;

import com.se2020.course.registration.wrapper.AuthUserWrapper;
import com.se2020.course.registration.entity.Permission;
import com.se2020.course.registration.entity.Role;
import com.se2020.course.registration.entity.Student;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.repository.PermissionRepository;
import com.se2020.course.registration.repository.RoleRepository;
import com.se2020.course.registration.repository.StudentRepository;
import com.se2020.course.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.se2020.course.registration.utils.SecurityUtils.hashPassword;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;



    @GetMapping("/user")
    public List<User> getAllUsers (@RequestBody User auth){
        Permission p = permissionRepository.findByPermission("view_user").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            return userRepository.findAll();
        }
        return new ArrayList<>();
    }

    @GetMapping("/user/{id}")
    public User getUser(@RequestBody User auth,
                        @PathVariable(name = "id") Long id){
        Permission p = permissionRepository.findByPermission("view_user").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            return userRepository.findById(id).orElse(null);
        }
        return new User();
    }

    @PostMapping("/login")
    public User login(@RequestBody User userLogin){
        if (userLogin.getEmail() != null && userLogin.getPassword() != null){
            final String hashedPass = hashPassword(userLogin.getPassword());
            List<User> users = userRepository.findByEmailAndPassword(userLogin.getEmail(), hashedPass);
            if (users.size() > 0){
                return users.get(0);
            }
        }
        return new User();
    }

    @PostMapping("/user")
    public String addUser(@RequestBody AuthUserWrapper authUserWrapper){
        User auth = authUserWrapper.getAuth();
        Permission p = permissionRepository.findByPermission("edit_user").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            User user = authUserWrapper.getUser();
            final String hashedPass = hashPassword(user.getPassword());
            user.setPassword(hashedPass);
            setStudentIdHelper(user);
            userRepository.save(user);
            return "Success";
        }
        return "Access denied";
    }

    @PutMapping("/user/{id}")
    public User updateUser(@RequestBody AuthUserWrapper authUserWrapper,
                           @PathVariable(name = "id") Long id){
        User auth = authUserWrapper.getAuth();
        Permission p = permissionRepository.findByPermission("edit_user").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission) {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                User newUser = authUserWrapper.getUser();
                final String hashedPass = hashPassword(newUser.getPassword());
                user.setPassword(hashedPass);
                user.setEmail(newUser.getEmail());
                user.setName(newUser.getName());
                user.setRole(newUser.getRole());
                setStudentIdHelper(user);
                userRepository.save(user);
            }
        }
        return new User();
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@RequestBody User auth,
                           @PathVariable(name = "id") Long id){
        Permission p = permissionRepository.findByPermission("edit_user").get(0);
        boolean hasPermission = hasPermission(auth, p);
        if (hasPermission){
            User user = userRepository.findById(id).orElse(null);
            if (user != null) { userRepository.deleteById(id); }
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

    private void setStudentIdHelper(User user){
        if (user.getRole().equals("student")){
            Student stu = new Student();
            studentRepository.save(stu);
            user.setStudentId(stu.getId());
        } else {user.setStudentId(-1L);}
    }

}

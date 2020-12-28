package com.se2020.course.registration.integration.controller;

import com.se2020.course.registration.controller.UserController;
import com.se2020.course.registration.entity.Permission;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.repository.PermissionRepository;
import com.se2020.course.registration.repository.UserRepository;
import com.se2020.course.registration.wrapper.AuthUserWrapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static com.se2020.course.registration.utils.SecurityUtils.hashPassword;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    public static final String EMAIL = "dung.nguyen.180009@student.fulbright.edu.vn";
    public static final String PASSWORD = "1234";

    @Autowired
    UserRepository userRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    TestRestTemplate restTemplate; // stand-alone browser as http



    @Test
    @Order(1)
    void contextLoads() {assertThat(userRepository).isNotNull();}

    @Test
    @Order(2)
    void testAddUser(){
        AuthUserWrapper authUserWrapper = new AuthUserWrapper();
        User auth = User.builder()
                .email("anh.nguyen.190005@student.fulbright.edu.vn")
                .password("1234")
                .build();

        User newUser = User.builder()
                .email("nhu.bui.200014@student.fulbright.edu.vn")
                .password(PASSWORD)
                .role("student")
                .build();

        authUserWrapper.setAuth(auth);
        authUserWrapper.setUser(newUser);

        String ret = restTemplate.postForObject("http://localhost:8080/user", authUserWrapper, String.class);
        assertThat(ret).isEqualTo("Success");
        assertThat(userRepository.findByEmailAndPasswordAndRole("nhu.bui.200014@student.fulbright.edu.vn", hashPassword(PASSWORD), "student")).isNotEmpty();

    }

    @Test
    @Order(3)
    void testAddUser2(){
        AuthUserWrapper authUserWrapper = new AuthUserWrapper();
        User auth = User.builder()
                .email("hoa.nguyen.190005@student.fulbright.edu.vn")
                .password("18961")
                .build();

        User newUser = User.builder()
                .email("nhu.bui.200014@student.fulbright.edu.vn")
                .password("1234")
                .role("student")
                .build();
        authUserWrapper.setAuth(auth);
        authUserWrapper.setUser(newUser);
        String ret = restTemplate.postForObject("http://localhost:8080/user", authUserWrapper, String.class);
        assertThat(ret).isEqualTo("Access denied");
    }

    @Test
    @Order(4)
    void testLogin(){
        User user = User.builder()
                .email(EMAIL)
                .password(hashPassword(PASSWORD))
                .name("Nguyen Thi Thuy Dung")
                .role("admin")
                .build();
        userRepository.save(user);

        User loginUser = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        User loginResult = restTemplate.postForObject("http://localhost:8080/login", loginUser, User.class);
        assertThat(loginResult).extracting(User::getEmail).isEqualTo(user.getEmail());
    }

    @Test
    @Order(5)
    void testLogin2(){
        User user = userRepository.findByEmailAndPassword(EMAIL, hashPassword(PASSWORD)).get(0);
        final User loginUser = User.builder()
                .email(EMAIL)
                .password("SJJHBUDHBA")
                .build();
        User loginResult = restTemplate.postForObject("http://localhost:8080/login", loginUser, User.class);
        assertThat(loginResult).extracting(User::getEmail).isNotEqualTo(user.getEmail());

    }

    @Test
    @Order(6)
    void testUpdateUser(){

        List<User> oldUsers = userRepository.findByEmailAndPassword(EMAIL, hashPassword(PASSWORD));
        assertThat(oldUsers).isNotEmpty();
        assertThat(oldUsers.get(0)).extracting(User::getRole).isNotEqualTo("student");

        AuthUserWrapper authUserWrapper = new AuthUserWrapper();
        User auth = User.builder()
                .email("anh.nguyen.190005@student.fulbright.edu.vn")
                .password("1234")
                .build();

        User updatedUser = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .role("student")
                .build();

        authUserWrapper.setAuth(auth);
        authUserWrapper.setUser(updatedUser);

        restTemplate.put("http://localhost:8080/user/{id}",authUserWrapper,oldUsers.get(0).getId());
        assertThat(userRepository.findByEmailAndPassword(EMAIL, hashPassword(PASSWORD)).get(0))
                .extracting((User::getRole)).isEqualTo("student");
    }

    
}

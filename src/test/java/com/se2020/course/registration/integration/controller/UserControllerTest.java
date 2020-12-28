package com.se2020.course.registration.unit.controller;

import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.repository.UserRepository;
import com.se2020.course.registration.wrapper.AuthUserWrapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    public static final String EMAIL = "dung.nguyen.180009@student.fulbright.edu.vn";
    public static final String PASSWORD = "1234";

    @Autowired
    UserRepository userRepository;
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
        assertThat(userRepository.findByEmailAndPasswordAndRole(EMAIL, PASSWORD, "student")).isNotEmpty();

    }

    @Test
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
    @Order(3)
    void testLogin(){
        User user = userRepository.findByEmailAndPassword(EMAIL, PASSWORD).get(0);

        final User loginUser = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        User loginResult = restTemplate.postForObject("http://localhost:8080/login", loginUser, User.class);
        assertThat(loginResult).extracting(User::getEmail).isEqualTo(user.getEmail());
    }

    @Test
    void testLogin2(){
        User user = userRepository.findByEmailAndPassword(EMAIL, PASSWORD).get(0);
        final User loginUser = User.builder()
                .email(EMAIL)
                .password("SJJHBUDHBA")
                .build();
        User loginResult = restTemplate.postForObject("http://localhost:8080/login", loginUser, User.class);
        assertThat(loginResult).extracting(User::getEmail).isEqualTo(user.getEmail());
    }

    @Test
    void testUpdateUser(){
        User oldUser = userRepository.findByEmailAndPassword(EMAIL, PASSWORD).get(0);
        assertThat("admin").isEqualTo(oldUser.getRole());
        AuthUserWrapper authUserWrapper = new AuthUserWrapper();
        User auth = User.builder()
                .email("anh.nguyen.190005@student.fulbright.edu.vn")
                .password("1234")
                .build();

        User updatedUser = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name("Nguyen Thi Thuy Dung")
                .role("student")
                .build();
        authUserWrapper.setAuth(auth);
        authUserWrapper.setUser(updatedUser);
        User ret = restTemplate.put("http://localhost:8080/user");
    }


}

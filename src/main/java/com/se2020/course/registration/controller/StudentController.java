package com.se2020.course.registration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    @GetMapping("/student/{id}")
    public String getStudent(@PathVariable(name = "id") String id){
        return "user " + id;
    }
}

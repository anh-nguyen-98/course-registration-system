package com.se2020.course.registration.wrapper;

import com.se2020.course.registration.entity.Course;
import com.se2020.course.registration.entity.User;

public class AuthCourseWrapper {
    private User auth;
    private Course course;

    public User getAuth() {
        return auth;
    }

    public void setAuth(User auth) {
        this.auth = auth;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}

package com.se2020.course.registration.wrapper;

import com.se2020.course.registration.entity.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthUserWrapper {

    private User auth;
    private User user;

    public User getAuth() {
        return auth;
    }

    public void setAuth(User auth) {
        this.auth = auth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

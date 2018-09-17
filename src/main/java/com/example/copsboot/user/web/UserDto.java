package com.example.copsboot.user.web;

import com.example.copsboot.user.User;
import com.example.copsboot.user.UserId;
import com.example.copsboot.user.UserRole;

import java.util.Set;

public class UserDto {
    private final UserId id;
    private final String email;
    private final Set<UserRole> roles;

    public UserDto(UserId id, String email, Set<UserRole> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public static UserDto fromUser(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getRoles());
    }

    public UserId getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }
}

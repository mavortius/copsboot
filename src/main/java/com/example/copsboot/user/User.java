package com.example.copsboot.user;

import com.example.copsboot.orm.jpa.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "copsboot_user")
public class User extends AbstractEntity<UserId> {

    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Set<UserRole> roles;

    protected User() {
    }

    public User(UserId id, String email, String password, @NotNull Set<UserRole> roles) {
        super(id);
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public static User createCaptain(UserId userId, String email, String password) {
        return new User(userId, email, password, Collections.singleton(UserRole.CAPTAIN));
    }

    public static User createOfficer(UserId userId, String email, String password) {
        return new User(userId, email, password, Collections.singleton(UserRole.OFFICER));
    }
}

package com.example.copsboot.user.web;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateOfficerParameters {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 1000)
    private String password;

    public CreateOfficerParameters() {
    }

    public CreateOfficerParameters(@NotNull @Email String email, @NotNull @Size(min = 6, max = 1000) String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

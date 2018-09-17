package com.example.copsboot;

import com.example.copsboot.user.UserService;
import com.example.infrastructure.SpringProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(SpringProfiles.DEV)
public class DevelopmentDbInitializer implements ApplicationRunner {

    private final UserService userService;

    @Autowired
    public DevelopmentDbInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createTestUsers();
    }

    private void createTestUsers() {
        userService.createOfficer("officer@example.com", "officer");
    }
}

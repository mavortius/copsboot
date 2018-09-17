package com.example.copsboot.user.web;

import com.example.copsboot.infrastructure.SpringProfiles;
import com.example.copsboot.infrastructure.security.StubUserDetailsService;
import com.example.copsboot.infrastructure.security.config.ResourceServerConfiguration;
import com.example.copsboot.infrastructure.security.config.SecurityConfiguration;
import com.example.copsboot.user.UserService;
import com.example.copsboot.user.Users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.example.copsboot.infrastructure.security.SecurityHelperForMockMvc.HEADER_AUTHORIZATION;
import static com.example.copsboot.infrastructure.security.SecurityHelperForMockMvc.bearer;
import static com.example.copsboot.infrastructure.security.SecurityHelperForMockMvc.obtainAccessToken;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserRestController.class)
@ActiveProfiles(SpringProfiles.TEST)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;

    @TestConfiguration
    @Import(ResourceServerConfiguration.class)
    static class TestConfig {

        @Bean
        public UserDetailsService userDetailsService() {
            return new StubUserDetailsService();
        }

        @Bean
        public TokenStore tokenStore() {
            return new InMemoryTokenStore();
        }

        @Bean
        public SecurityConfiguration securityConfiguration() {
            return new SecurityConfiguration();
        }

    }
    @Test
    public void givenNotAuthenticated_whenAskingMyDetails_forbidden() throws Exception {
        mvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenAuthenticatedAsOfficer_whenAskingMyDetails_detailsReturned() throws Exception {
        String accessToken = obtainAccessToken(mvc, Users.OFFICER_EMAIL, Users.OFFICER_PASSWORD);

        when(service.getUser(Users.officer().getId())).thenReturn(Optional.of(Users.officer()));

        mvc.perform(get("/api/users/me")
                .header(HEADER_AUTHORIZATION, bearer(accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(Users.OFFICER_EMAIL))
                .andExpect(jsonPath("roles").isArray())
                .andExpect(jsonPath("roles[0]").value("OFFICER"));
    }

    @Test
    public void createOfficer() throws Exception {
        String email = "wim.deblauwe@example.com";
        String password = "my-super-secret-pwd";

        CreateOfficerParameters parameters = new CreateOfficerParameters();
        parameters.setEmail(email);
        parameters.setPassword(password);

        when(service.createOfficer(email, password)).thenReturn(Users.newOfficer(email, password));

        mvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(parameters)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("roles").isArray())
                .andExpect(jsonPath("roles[0]").value("OFFICER"));

        verify(service).createOfficer(email, password);
    }
}
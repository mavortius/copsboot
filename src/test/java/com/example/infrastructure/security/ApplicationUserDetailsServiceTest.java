package com.example.infrastructure.security;

import com.example.copsboot.user.UserRepository;
import com.example.copsboot.user.Users;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationUserDetailsServiceTest {

    @Test
    public void givenExistingUsername_whenLoadingUser_userIsReturned() {
        UserRepository repository = mock(UserRepository.class);
        ApplicationUserDetailsService service = new ApplicationUserDetailsService(repository);

        when(repository.findByEmailIgnoreCase(Users.OFFICER_EMAIL))
                .thenReturn(Optional.of(Users.officer()));

        UserDetails userDetails = service.loadUserByUsername(Users.OFFICER_EMAIL);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(Users.OFFICER_EMAIL);
        assertThat(userDetails.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_OFFICER");
        assertThat(userDetails).isInstanceOfSatisfying(ApplicationUserDetails.class,
                applicationUserDetails -> {
                    assertThat(applicationUserDetails.getUserId()).isEqualTo(Users.officer().getId());
                });
    }

    @Test(expected = UsernameNotFoundException.class)
    public void givenNotExistingUsername_whenLoadingUser_exceptionThrown() {
        UserRepository repository = mock(UserRepository.class);
        ApplicationUserDetailsService service = new ApplicationUserDetailsService(repository);

        when(repository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        service.loadUserByUsername("i@donotexist.com");
    }
}
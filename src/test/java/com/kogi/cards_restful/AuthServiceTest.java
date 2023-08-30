package com.kogi.cards_restful;

import com.kogi.cards_restful.models.ERole;
import com.kogi.cards_restful.models.Role;
import com.kogi.cards_restful.payload.request.LoginRequest;
import com.kogi.cards_restful.payload.request.SignupRequest;
import com.kogi.cards_restful.repository.RoleRepository;
import com.kogi.cards_restful.repository.UserRepository;
import com.kogi.cards_restful.security.jwt.JwtUtils;
import com.kogi.cards_restful.security.services.UserDetailsImpl;
import com.kogi.cards_restful.services.AuthServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Test
    public void testSignIn() {
        String email = "user@example.com";
        String password = "password";

        LoginRequest loginRequest = new LoginRequest();

        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);

        String jwtToken = "exampleJwtToken";
        Mockito.when(jwtUtils.generateJwtToken(Mockito.any(Authentication.class))).thenReturn(jwtToken);

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, email, password, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<?> response = authService.signIn(loginRequest);

        Assertions.assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
    }

    @Test(expected = BadCredentialsException.class)
    public void testWrongCredentials() throws BadCredentialsException{
        String email = "user@example.com";
        String password = "password";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = authService.signIn(loginRequest);
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(), response);
    }


    @Test
    public void testSignUpSuccess() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("user@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(Collections.emptySet());

        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(encoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        Role memberRole = new Role(ERole.ROLE_MEMBER);
        Mockito.when(roleRepository.findByName(Mockito.any(ERole.class))).thenReturn(Optional.of(memberRole));

        ResponseEntity<?> response = authService.signUp(signUpRequest);

        Assertions.assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testSignUpWithAdminRole() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("user@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(Set.of("admin"));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(encoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        Role adminRole = new Role(ERole.ROLE_ADMIN);
        Mockito.when(roleRepository.findByName(Mockito.any(ERole.class))).thenReturn(Optional.of(adminRole));

        ResponseEntity<?> response = authService.signUp(signUpRequest);

        Assertions.assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testSignUpWithEmailExists() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("user@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(Set.of("admin"));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        ResponseEntity<?> response = authService.signUp(signUpRequest);

        Assertions.assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }
}

package com.kogi.cards_restful.controllers;

import com.kogi.cards_restful.payload.request.LoginRequest;
import com.kogi.cards_restful.payload.request.SignupRequest;
import com.kogi.cards_restful.payload.response.JwtResponse;
import com.kogi.cards_restful.payload.response.MessageResponse;
import com.kogi.cards_restful.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    /**
     * Used to sign in a user.
     * @param loginRequest
     * @return 200 OK and JwtResponse if the user is authenticated successfully, 401 UNAUTHORIZED if the email or password is incorrect.
     */
    @Operation(summary = "SignIn A User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Sign In",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))})})
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    /**
     * Used to sign up a user.
     * @param signUpRequest
     * @return 200 OK if the user is registered successfully, 400 BAD REQUEST if the email already exists.
     */
    @Operation(summary = "SignUp A User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Sign Up",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))})})
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }
}

package com.kogi.cards_restful.services;

import com.kogi.cards_restful.payload.request.LoginRequest;
import com.kogi.cards_restful.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    /***
     * This method is used to authenticate a user.
     * @param loginRequest
     * @return 200 OK and JwtResponse if the user is authenticated successfully, 401 UNAUTHORIZED if the email or password is incorrect.
     */
    ResponseEntity<?> signIn(LoginRequest loginRequest);
    /***
     * This method is used to register a new user.
     * @param signUpRequest
     * @return 200 OK if the user is registered successfully, 400 BAD REQUEST if the email already exists.
     */
    ResponseEntity<?> signUp(SignupRequest signUpRequest);
}

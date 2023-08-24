package com.kogi.cards_restful.services;

import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.payload.request.LoginRequest;
import com.kogi.cards_restful.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> signIn(LoginRequest loginRequest);
    ResponseEntity<?> signUp(SignupRequest signUpRequest);
}

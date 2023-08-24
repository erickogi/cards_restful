package com.kogi.cards_restful.services;

import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.payload.request.PatchCardRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface CardService {
    ResponseEntity<?> createCard(CreateCardRequest createCardRequest, String authorizationHeader);

    ResponseEntity<?> list(Pageable pageable, String jwtToken);

    ResponseEntity<?> search(Pageable pageable, String jwtToken, String name, String description, String color, LocalDate date, String status);

    ResponseEntity<?> getOne(Long id, String jwtToken);

    ResponseEntity<?> patchCard(Long id, PatchCardRequest partialUpdateDto, String jwtToken);

    ResponseEntity<?> deleteCard(Long id, String jwtToken);

}

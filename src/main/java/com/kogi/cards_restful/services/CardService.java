package com.kogi.cards_restful.services;

import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.payload.request.PatchCardRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface CardService {

    /**
     * This method is used to create a card.
     * @param createCardRequest
     * @param authorizationHeader
     * @return 200 OK with the created card if the card is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> createCard(CreateCardRequest createCardRequest, String authorizationHeader);
    /**
     * This method is used to list cards.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of cards(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> list(Pageable pageable, String jwtToken);
    /**
     * This method is used to search cards.
     * @param pageable
     * @param jwtToken
     * @param name
     * @param description
     * @param color
     * @param date
     * @param status
     * @return 200 OK with a list of cards(Can be empty) if the search is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> search(Pageable pageable, String jwtToken, String name, String description, String color, LocalDate date, String status);
    /**
     * This method is used to get a card.
     * @param id
     * @param jwtToken
     * @return 200 OK with card object if the card is found, 404 NOT FOUND if the card does not exist and 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> getOne(Long id, String jwtToken);
    /**
     * This method is used to update a card.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the card is updated successfully, 404 NOT FOUND if the card does not exist and 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> patchCard(Long id, PatchCardRequest partialUpdateDto, String jwtToken);
    /**
     * This method is used to delete a card.
     * @param id
     * @param jwtToken
     * @return 200 OK if the card is deleted successfully, 404 NOT FOUND if the card does not exist.
     */
    ResponseEntity<?> deleteCard(Long id, String jwtToken);

}

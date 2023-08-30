package com.kogi.cards_restful.controllers;

import com.kogi.cards_restful.models.Card;
import com.kogi.cards_restful.payload.request.CardColorFormat;
import com.kogi.cards_restful.payload.request.CardStatusFormat;
import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.payload.request.PatchCardRequest;
import com.kogi.cards_restful.payload.response.CardPage;
import com.kogi.cards_restful.payload.response.CardResponse;
import com.kogi.cards_restful.payload.response.MessageResponse;
import com.kogi.cards_restful.payload.response.RequestValidationErrorResponse;
import com.kogi.cards_restful.services.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/card")
@Validated
public class CardController {
    @Autowired
    private CardService cardService;

    /**
     * Used to create a card.
     * @param authorizationHeader
     * @param createCardRequest
     * @param bindingResult
     * @return 200 OK and CardResponse if the card is created successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "Create a Card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @PostMapping("/create")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<?> createCard(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CreateCardRequest createCardRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return cardService.createCard(createCardRequest, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to list all cards.
     * @param authorizationHeader
     * @param pageable
     * @return 200 OK and CardPage if the cards are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Cards accessible to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardPage.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/list")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<?> listCards(
            @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return cardService.list(pageable, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to search cards.
     * @param authorizationHeader
     * @param name
     * @param description
     * @param color
     * @param date
     * @param status
     * @param pageable
     * @return 200 OK and CardPage if the cards are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "Cards by name,description,color,date,status. Sorted by any field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Cards accessible to user filtered and sorted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardPage.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/search")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchCards(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "color", required = false) @CardColorFormat  String color,
            @RequestParam(name = "date", required = false) LocalDate date,
            @RequestParam(name = "status", required = false)@CardStatusFormat String status,
            Pageable pageable) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return cardService.search(pageable, jwtToken, name, description, color, date, status);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to get a card by its id.
     * @param authorizationHeader
     * @param id
     * @return 200 OK and CardResponse if the card is retrieved successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the card is not found because of role
     */
    @Operation(summary = "Get one card by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Card.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Card Not Found for given id and user",
                    content = @Content)
    })
    @GetMapping("/card")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<?> card(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(name = "id") Long id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return cardService.getOne(id, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to update a card.
     * @param authorizationHeader
     * @param id
     * @param partialUpdateDto
     * @param bindingResult
     * @return 200 OK and CardResponse if the card is updated successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the card is not found because of role
     */
    @Operation(summary = "Update a Card characteristics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Card Not Found for given id and user",
                    content = @Content)
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> patchCard(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @Valid @RequestBody PatchCardRequest partialUpdateDto
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return cardService.patchCard(id, partialUpdateDto, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }
    /**
     * Used to delete a card.
     * @param authorizationHeader
     * @param id
     * @param bindingResult
     * @return 200 OK and MessageResponse if the card is deleted successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the card is not found because of role
     */
    @Operation(summary = "Delete a Card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Card Not Found for given id and user",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCard(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @PathVariable Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return cardService.deleteCard(id, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }
}



package com.kogi.cards_restful;

import com.kogi.cards_restful.controllers.CardController;
import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.services.CardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CardControllerTest {

    @InjectMocks
    private CardController cardController;

    @Mock
    private CardService cardService;

    @Test
    public void testCreateCard_ValidTokenAndRequest() {
        String validToken = "valid-token";
        CreateCardRequest request = new CreateCardRequest(/* ... */);
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "createCardRequest");
        when(cardService.createCard(any(CreateCardRequest.class), eq(validToken)))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<?> response = cardController.createCard("Bearer " + validToken, request, bindingResult);
        verify(cardService, times(1)).createCard(any(CreateCardRequest.class), eq(validToken));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateCard_InvalidToken() {
        CreateCardRequest request = new CreateCardRequest(/* ... */);
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "createCardRequest");
        ResponseEntity<?> response = cardController.createCard("InvalidToken", request, bindingResult);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testCreateCard_ValidationErrors() {
        String validToken = "valid-token";
        CreateCardRequest request = new CreateCardRequest(/* ... */);
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "createCardRequest");
        bindingResult.reject("field.error", "Field error message");
        ResponseEntity<?> response = cardController.createCard("Bearer " + validToken, request, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testListCards_ValidToken() {
        String validToken = "valid-token";
        Pageable pageable = Pageable.unpaged();
        when(cardService.list(any(Pageable.class), eq(validToken)))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<?> response = cardController.listCards("Bearer " + validToken, pageable);
        verify(cardService, times(1)).list(any(Pageable.class), eq(validToken));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testListCards_InvalidToken() {
        Pageable pageable = Pageable.unpaged();
        ResponseEntity<?> response = cardController.listCards("InvalidToken", pageable);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testSearchCards_ValidToken() {
        String validToken = "valid-token";
        Pageable pageable = Pageable.unpaged();
        String name = "CardName";
        String description = "CardDescription";
        String color = "546464";
        LocalDate date = LocalDate.now();
        String status = "InProgress";
        when(cardService.search(any(Pageable.class), eq(validToken), eq(name), eq(description), eq(color), eq(date), eq(status)))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = cardController.searchCards(
                "Bearer " + validToken,
                name,
                description,
                color,
                date,
                status,
                pageable
        );

        verify(cardService, times(1)).search(
                any(Pageable.class),
                eq(validToken),
                eq(name),
                eq(description),
                eq(color),
                eq(date),
                eq(status)
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCard_ValidToken() {
        String validToken = "valid-token";
        Long cardId = 123L;
        when(cardService.getOne(eq(cardId), eq(validToken)))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<?> response = cardController.card("Bearer " + validToken, cardId);
        verify(cardService, times(1)).getOne(eq(cardId), eq(validToken));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCard_InvalidToken() {
        Long cardId = 123L;
        ResponseEntity<?> response = cardController.card("InvalidToken", cardId);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}


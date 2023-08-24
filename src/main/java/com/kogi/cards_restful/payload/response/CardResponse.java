package com.kogi.cards_restful.payload.response;

import com.kogi.cards_restful.models.Card;

public class CardResponse {
  private String message;
  private Card card;

  public CardResponse(String message,Card card) {
    this.message = message;
    this.card = card;
  }

  public Card getCard() {
    return card;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

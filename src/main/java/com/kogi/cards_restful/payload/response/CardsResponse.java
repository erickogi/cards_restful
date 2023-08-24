package com.kogi.cards_restful.payload.response;

import com.kogi.cards_restful.models.Card;
import org.springframework.data.domain.Page;

import java.util.Set;

public class CardsResponse {
  private String message;
  private Page<Card> cards;

  public CardsResponse(String message, Page<Card> cards) {
    this.message = message;
    this.cards = cards;
  }

  public Page<Card> getCards() {
    return cards;
  }

  public void setCards(Page<Card> cards) {
    this.cards = cards;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

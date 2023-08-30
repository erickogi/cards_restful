package com.kogi.cards_restful.models;

/**
 * This class is used to build a card object.
 */
public class CardBuilder {
    private String name;
    private String description;
    private String color;
    private CardStatus cardStatus;
    private User creator;

    public CardBuilder() {
    }

    public CardBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CardBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CardBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public CardBuilder withCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
        return this;
    }

    public CardBuilder withCreator(User creator) {
        this.creator = creator;
        return this;
    }

    public Card build() {
        return new Card(name, description, color, cardStatus, creator);
    }
}


package com.kogi.cards_restful.payload.response;

import com.kogi.cards_restful.models.Card;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CardPage extends PageImpl<Card> {
    public CardPage(List<Card> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
package com.kogi.cards_restful;

import com.kogi.cards_restful.models.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageMockFactory {

    public static Page<Card> createPageMock(List<Card> content, Pageable pageable, long totalElements) {
        return new PageImpl<>(content, pageable, totalElements);
    }
}

package com.kogi.cards_restful.services;

import com.kogi.cards_restful.models.*;
import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.payload.request.PatchCardRequest;
import com.kogi.cards_restful.payload.response.CardResponse;
import com.kogi.cards_restful.payload.response.MessageResponse;
import com.kogi.cards_restful.repository.CardRepository;
import com.kogi.cards_restful.repository.UserRepository;
import com.kogi.cards_restful.security.jwt.JwtUtils;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static com.kogi.cards_restful.models.ERole.ROLE_ADMIN;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    private Optional<User> getUser(String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        return userRepository.findByEmail(email);
    }

    private boolean isAdmin(Set<Role> roles) {
        for (Role role : roles) {
            if (role.getName() == ROLE_ADMIN) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<?> createCard(CreateCardRequest createCardRequest, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Card newCard = new CardBuilder()
                .withName(createCardRequest.getName())
                .withDescription(createCardRequest.getDescription())
                .withColor(createCardRequest.getColor())
                .withCardStatus(CardStatus.TODO)
                .withCreator(creator.get())
                .build();
        Card createdCard = cardRepository.save(newCard);
        return ResponseEntity.ok(new CardResponse("Card created successfully!", createdCard));
    }

    @Override
    public ResponseEntity<?> list(Pageable pageable, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Page<Card> cards;

        if (isAdmin(creator.get().getRoles())) {
            cards = cardRepository.findAll(pageable);
        } else {
            cards = cardRepository.findByCreator(creator.get(), pageable);
        }
        return ResponseEntity.ok(cards);
    }

    @Override
    public ResponseEntity<?> search(Pageable pageable, String jwtToken, String name, String description, String color, LocalDate date, String status) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Specification<Card> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (!isCreatorAnAdmin) {
                predicate = cb.and(predicate, cb.equal(root.get("creator"), creator.get()));
            }
            if (name != null) {
                predicate = cb.and(predicate, cb.equal(root.get("name"), name));
            }

            if (description != null) {
                predicate = cb.and(predicate, cb.equal(root.get("description"), description));
            }

            if (color != null) {
                predicate = cb.and(predicate, cb.equal(root.get("color"), color));
            }

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("cardStatus"), CardStatus.valueOf(status)));
            }

            if (date != null) {
                Expression<LocalDate> dateExpression = cb.function(
                        "DATE", LocalDate.class, root.get("createdAt")
                );
                predicate = cb.and(predicate, cb.equal(dateExpression, date));
            }

            return predicate;
        };
        Page<Card> cards = cardRepository.findAll(specification, pageable);
        return ResponseEntity.ok(cards);
    }

    @Override
    public ResponseEntity<?> getOne(Long id, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Card> card;
        if(isCreatorAnAdmin){
            card = cardRepository.findById(id);
        }else {
            card = cardRepository.findByIdAndCreator(id,creator.get());
        }
        if(card.isPresent()) {
            return ResponseEntity.ok(card);
        }else {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @Override
    public ResponseEntity<?> patchCard(Long id, PatchCardRequest partialUpdateDto, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Card> card;
        if(isCreatorAnAdmin){
            card = cardRepository.findById(id);
        }else {
            card = cardRepository.findByIdAndCreator(id,creator.get());
        }

        if(card.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        Card cardToUpdate = card.get();
        if (partialUpdateDto.getName() != null) {
            cardToUpdate.setName(partialUpdateDto.getName());
        }
        if (partialUpdateDto.getColor() != null) {
            cardToUpdate.setColor(partialUpdateDto.getColor());
        }
        if (partialUpdateDto.getDescription() != null) {
            cardToUpdate.setDescription(partialUpdateDto.getDescription());
        }
        if (partialUpdateDto.getStatus() != null) {
            cardToUpdate.setCardStatus(CardStatus.valueOf(partialUpdateDto.getStatus()));
        }
        Card updatedCard = cardRepository.save(cardToUpdate);
        return ResponseEntity.ok(new CardResponse("Card updated successfully!", updatedCard));

    }

    @Override
    public ResponseEntity<?> deleteCard(Long id, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Card> card;
        if(isCreatorAnAdmin){
            card = cardRepository.findById(id);
        }else {
            card = cardRepository.findByIdAndCreator(id,creator.get());
        }

        if(card.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        cardRepository.delete(card.get());
        return ResponseEntity.ok(new MessageResponse("Card deleted"));

    }
}

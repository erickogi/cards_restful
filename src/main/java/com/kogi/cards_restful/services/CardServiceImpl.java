package com.kogi.cards_restful.services;

import com.kogi.cards_restful.models.*;
import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.payload.request.PatchCardRequest;
import com.kogi.cards_restful.payload.response.CardResponse;
import com.kogi.cards_restful.payload.response.MessageResponse;
import com.kogi.cards_restful.repository.CardRepository;
import com.kogi.cards_restful.repository.UserRepository;
import com.kogi.cards_restful.security.jwt.JwtUtils;
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
     /**
     * This method is used to get a user from a jwt token.
     * @param jwtToken
     * @return an optional user object.
     */
    private Optional<User> getUser(String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        return userRepository.findByEmail(email);
    }

    /**
     * This method is used to check if a user is an admin.
     * @param roles
     * @return true if the user is an admin, false otherwise.
     */
    private boolean isAdmin(Set<Role> roles) {
        for (Role role : roles) {
            if (role.getName() == ROLE_ADMIN) {
                return true;
            }
        }
        return false;
    }
     /**
     * This method is used to create a card.
     * @param createCardRequest
     * @param jwtToken
     * @return 200 OK with the created card if the card is created successfully, 400 BAD REQUEST if the user does not exist.
     */
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
     /**
     * This method is used to list cards.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of cards(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
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

    /**
     * This method is used to get a card.
     * @param id
     * @param jwtToken
     * @return 200 OK with card object if the card is found, 404 NOT FOUND if the card does not exist and 400 BAD REQUEST if the user does not exist.
     */
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

    /**
     * This method is used to update a card.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the card is updated successfully, 404 NOT FOUND if the card does not exist and 400 BAD REQUEST if the user does not exist.
     */
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

    /**
     * This method is used to delete a card.
     * @param id
     * @param jwtToken
     * @return 200 OK if the card is deleted successfully, 404 NOT FOUND if the card does not exist.
     */
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

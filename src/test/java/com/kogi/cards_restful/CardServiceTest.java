package com.kogi.cards_restful;

import com.kogi.cards_restful.models.Card;
import com.kogi.cards_restful.models.Role;
import com.kogi.cards_restful.models.User;
import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.payload.request.PatchCardRequest;
import com.kogi.cards_restful.payload.response.MessageResponse;
import com.kogi.cards_restful.repository.CardRepository;
import com.kogi.cards_restful.repository.UserRepository;
import com.kogi.cards_restful.security.jwt.JwtUtils;
import com.kogi.cards_restful.services.CardServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static com.kogi.cards_restful.models.ERole.ROLE_ADMIN;
import static com.kogi.cards_restful.models.ERole.ROLE_MEMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    public void testCreateCard() {
        String jwtToken = "exampleJwtToken";
        CreateCardRequest createCardRequest = new CreateCardRequest();

        User mockUser = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Card mockCard = new Card();
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(mockCard);

        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");


        ResponseEntity<?> response = cardService.createCard(createCardRequest, jwtToken);

        Mockito.verify(userRepository, times(1)).findByEmail(Mockito.anyString());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateCardWrongToken() {
        String jwtToken = "exampleJwtToken";
        CreateCardRequest createCardRequest = new CreateCardRequest();

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Card mockCard = new Card();
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(mockCard);

        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");


        ResponseEntity<?> response = cardService.createCard(createCardRequest, jwtToken);

        Mockito.verify(userRepository, times(1)).findByEmail(Mockito.anyString());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testListForAdmin() {
        String jwtToken = "exampleJwtToken";
        Pageable pageable = Mockito.mock(Pageable.class);

        Role adminRole = new Role(ROLE_ADMIN);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));
        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        List<Card> cardList = new ArrayList<>();
        cardList.add(mockCard);
        Page<Card> mockCards = PageMockFactory.createPageMock(cardList, pageable, cardList.size());

        Mockito.when(cardRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockCards);

        ResponseEntity<?> response = cardService.list(pageable, jwtToken);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(ResponseEntity.ok(mockCards).getBody(), response.getBody());
    }


    @Test
    public void testListForAdminNotAdmin() {
        String jwtToken = "exampleJwtToken";
        Pageable pageable = Mockito.mock(Pageable.class);

        Role adminRole = new Role(ROLE_MEMBER);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));
        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        List<Card> cardList = new ArrayList<>();
        cardList.add(mockCard);
        Page<Card> mockCards = PageMockFactory.createPageMock(cardList, pageable, cardList.size());

        Mockito.when(cardRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockCards);

        ResponseEntity<?> response = cardService.list(pageable, jwtToken);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(ResponseEntity.ok(null).getBody(), response.getBody());
    }

    @Test
    public void testListForMember() {
        String jwtToken = "exampleJwtToken";
        Pageable pageable = Mockito.mock(Pageable.class);

        Role role = new Role(ROLE_MEMBER);
        Set<Role> roles = Collections.singleton(role);
        User mockUser = new User();
        mockUser.setRoles(roles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));
        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        List<Card> cardList = new ArrayList<>();
        cardList.add(mockCard);
        Page<Card> mockCards = PageMockFactory.createPageMock(cardList, pageable, cardList.size());

        Mockito.when(cardRepository.findByCreator(Mockito.any(User.class),Mockito.any(Pageable.class))).thenReturn(mockCards);

        ResponseEntity<?> response = cardService.list(pageable, jwtToken);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
        Assertions.assertEquals(ResponseEntity.ok(mockCards).getBody(), response.getBody());
    }


    @Test
    public void testListForMemberNotMember() {
        String jwtToken = "exampleJwtToken";
        Pageable pageable = Mockito.mock(Pageable.class);

        Role adminRole = new Role(ROLE_ADMIN);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));
        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        List<Card> cardList = new ArrayList<>();
        cardList.add(mockCard);
        Page<Card> mockCards = PageMockFactory.createPageMock(cardList, pageable, cardList.size());

        Mockito.when(cardRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockCards);

        ResponseEntity<?> response = cardService.list(pageable, jwtToken);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(ResponseEntity.ok(mockCards).getBody(), response.getBody());
    }

    @Test
    public void testSearch() {
        String jwtToken = "exampleJwtToken";
        Pageable pageable = Mockito.mock(Pageable.class);

        Role adminRole = new Role(ROLE_ADMIN);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Page mockCards = mock(Page.class);
        when(cardRepository.findAll(Mockito.any(Specification.class), any(Pageable.class))).thenReturn(mockCards);

        ResponseEntity<?> response = cardService.search(pageable, jwtToken, "Card Name", null, "Red", null, "TODO");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testGetOneForAdmin() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;

        Role adminRole = new Role(ROLE_ADMIN);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Card mockCard = new Card();
        Mockito.when(cardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockCard));

        ResponseEntity<?> response = cardService.getOne(cardId, jwtToken);

        Assertions.assertEquals(ResponseEntity.ok(Optional.of(mockCard)).getBody(), response.getBody());
    }

    @Test
    public void testGetOneForNonAdminWithAdminCard() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;

        Role adminRole = new Role(ROLE_MEMBER);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        Mockito.when(cardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockCard));

        ResponseEntity<?> response = cardService.getOne(cardId, jwtToken);

        Assertions.assertEquals(ResponseEntity.ok(null).getBody(), response.getBody());
    }

    @Test
    public void testGetOneNotFound() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;

        User mockUser = new User();
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Mockito.when(cardRepository.findByIdAndCreator(Mockito.anyLong(), Mockito.any(User.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = cardService.getOne(cardId, jwtToken);

        Assertions.assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testPatchCardForAdmin() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;
        PatchCardRequest partialUpdateDto = new PatchCardRequest();
        partialUpdateDto.setName("Updated Name");
        partialUpdateDto.setColor("#46464");

        Role adminRole = new Role(ROLE_ADMIN);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        Mockito.when(cardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockCard));

        Card updatedCard = new Card();
        updatedCard.setCreator(mockUser);
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(updatedCard);

        ResponseEntity<?> response = cardService.patchCard(cardId, partialUpdateDto, jwtToken);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testPatchCardForNonAdminCardNotFound() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;
        PatchCardRequest partialUpdateDto = new PatchCardRequest();
        partialUpdateDto.setName("Updated Name");
        partialUpdateDto.setColor("Blue");

        Role adminRole = new Role(ROLE_MEMBER);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");

        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        Mockito.when(cardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockCard));

        Card updatedCard = new Card();
        mockCard.setCreator(mockUser);
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(updatedCard);

        ResponseEntity<?> response = cardService.patchCard(cardId, partialUpdateDto, jwtToken);

        Assertions.assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
    }


    @Test
    public void testDeleteCardForAdmin() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;

        Role adminRole = new Role(ROLE_ADMIN);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        Mockito.when(cardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockCard));

        ResponseEntity<?> response = cardService.deleteCard(cardId, jwtToken);

        Assertions.assertEquals(ResponseEntity.ok(new MessageResponse("Card deleted")).getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testDeleteCardForMember() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;

        Role adminRole = new Role(ROLE_MEMBER);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Card mockCard = new Card();
        mockCard.setCreator(mockUser);
        Mockito.when(cardRepository.findByIdAndCreator(Mockito.anyLong(), Mockito.any(User.class))).thenReturn(Optional.of(mockCard));

        ResponseEntity<?> response = cardService.deleteCard(cardId, jwtToken);

        Assertions.assertEquals(ResponseEntity.ok(new MessageResponse("Card deleted")).getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testDeleteCardForNonAdminWithAdminCard() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;

        Role adminRole = new Role(ROLE_MEMBER);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));


        Card mockCard = new Card();
        Mockito.when(cardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockCard));

        ResponseEntity<?> response = cardService.deleteCard(cardId, jwtToken);

        Assertions.assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testDeleteCardNotFound() {
        String jwtToken = "exampleJwtToken";
        Long cardId = 1L;

        Role adminRole = new Role(ROLE_MEMBER);
        Set<Role> adminRoles = Collections.singleton(adminRole);
        User mockUser = new User();
        mockUser.setRoles(adminRoles);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("exampleEmail");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockUser));

        Mockito.when(cardRepository.findByIdAndCreator(Mockito.anyLong(), Mockito.any(User.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = cardService.deleteCard(cardId, jwtToken);

        Assertions.assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
    }
}

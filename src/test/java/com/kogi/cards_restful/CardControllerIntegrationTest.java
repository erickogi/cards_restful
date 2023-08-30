package com.kogi.cards_restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kogi.cards_restful.models.ERole;
import com.kogi.cards_restful.payload.request.CreateCardRequest;
import com.kogi.cards_restful.payload.request.LoginRequest;
import com.kogi.cards_restful.payload.request.PatchCardRequest;
import com.kogi.cards_restful.payload.response.JwtResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testCreateCard() throws Exception {
        CreateCardRequest request = new CreateCardRequest();
        request.setName("Test Card");
        request.setDescription("Test Card for integration testing");
        request.setColor("#000000");
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/card/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Card created successfully!"));
    }
    @Test
    public void testCreateCardUnauthorized() throws Exception {
        CreateCardRequest request = new CreateCardRequest();
        request.setName("Test Card");
        request.setDescription("Test Card for integration testing");
        request.setColor("#000000");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Full authentication is required to access this resource"));
    }
    @Test
    public void testListCards() throws Exception {
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/list")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testListCardsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Full authentication is required to access this resource"));
    }
    @Test
    public void testSearchCards() throws Exception {
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/search")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("date", "2023-08-30")
                        .param("status", "TODO")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testSearchCardsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/search")
                        .param("name", "Card Name")
                        .param("description", "Card Description")
                        .param("color", "Blue")
                        .param("date", "2023-08-01")
                        .param("status", "TODO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Full authentication is required to access this resource"));
    }
    @Test
    public void testGetCard() throws Exception {
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        long cardId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/card")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("id", Long.toString(cardId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testGetCardNotFound() throws Exception {
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        long cardId = -1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/card")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("id", Long.toString(cardId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testGetCardUnauthorized() throws Exception {
        long cardId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/card/card")
                        .param("id", Long.toString(cardId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Full authentication is required to access this resource"));
    }
    @Test
    public void testPatchCard() throws Exception {
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        Long cardId = 1L;
        PatchCardRequest request = new PatchCardRequest();
        request.setName("Updated Card Name");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/card/update/{id}", cardId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testPatchCardNotFound() throws Exception {
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        Long cardId = -1L;
        PatchCardRequest request = new PatchCardRequest();
        request.setName("Updated Card Name");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/card/update/{id}", cardId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testPatchCardUnauthorized() throws Exception {
        Long cardId = 1L;
        PatchCardRequest request = new PatchCardRequest();
        request.setName("Updated Card Name");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/card/update/{id}", cardId)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Full authentication is required to access this resource"));
    }
    @Test
    public void testDeleteCard() throws Exception {
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        Long cardId = 5L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/card/delete/{id}", cardId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Card deleted"));
    }
    @Test
    public void testDeleteCardNotFound() throws Exception {
        String jwtToken = signIn(ERole.ROLE_ADMIN);
        Long cardId = -5L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/card/delete/{id}", cardId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testDeleteCardUnauthorized() throws Exception {
        Long cardId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/card/delete/{id}", cardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Full authentication is required to access this resource"));
    }




    public String signIn(ERole roleAdmin) throws Exception {
        LoginRequest request = new LoginRequest();
        if(roleAdmin == ERole.ROLE_ADMIN) {
            request.setEmail("admin@gmail.com");
        }else{
            request.setEmail("member@gmail.com");
        }
        request.setPassword("123456789");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        if(resultActions.andReturn().getResponse().getStatus() == HttpStatus.OK.value()){
            String response = resultActions.andReturn().getResponse().getContentAsString();
            JwtResponse jwtResponse = new ObjectMapper().readValue(response, JwtResponse.class);
            return  jwtResponse.getAccessToken();
        }
        return null;
    }

    public String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

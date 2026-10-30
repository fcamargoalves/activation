package com.deroyque.controller;

import com.deroyque.dto.CreateActivationRequest;
import com.deroyque.service.ActivationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivationRequestController.class)
class ActivationRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActivationService activationService;

    @Test
    void shouldReturnBadRequestWhenExternalIdIsNull() throws Exception {
        CreateActivationRequest request = new CreateActivationRequest(
                null, "CLIENTE-01",
                "PRODUTO-01");

        mockMvc.perform(post("/api/v1/requests").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCustomerIdIsNull() throws Exception {
        CreateActivationRequest request = new CreateActivationRequest(
                "EXT-001", null,
                "PRODUTO-01");

        mockMvc.perform(post("/api/v1/requests").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenProductCodeIsNull() throws Exception {
        CreateActivationRequest request = new CreateActivationRequest(
                "EXT-001",
                "CLIENTE-01", null);

        mockMvc.perform(post("/api/v1/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenExternalIdIsBlank() throws Exception {
        CreateActivationRequest request = new CreateActivationRequest(
                "",
                "CLIENTE-01",
                "PRODUTO-01"
        );

        mockMvc.perform(
                post("/api/v1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCustomerIdIsBlank() throws Exception {

        CreateActivationRequest request = new CreateActivationRequest(
                "EXT-001",
                "",
                "PRODUTO-01"
        );

        mockMvc.perform(
                post("/api/v1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenProductCodeIsBlank() throws Exception {

        CreateActivationRequest request = new CreateActivationRequest(
                "EXT-001",
                "CLIENTE-01",
                ""
        );

        mockMvc.perform(
                post("/api/v1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


}

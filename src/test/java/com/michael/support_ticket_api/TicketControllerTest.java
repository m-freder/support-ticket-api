package com.michael.support_ticket_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@Import(GlobalExceptionHandler.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService service;

    @Test
    void getAll_returnsOkAndTicketList() throws Exception {
        Ticket ticket = new Ticket(1L, "Login issue", "Unable to login", "OPEN");
        when(service.getAll()).thenReturn(List.of(ticket));

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Login issue"))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    void getById_whenMissing_returnsNotFoundProblemDetail() throws Exception {
        when(service.getById(42L)).thenThrow(new TicketNotFoundException(42L));

        mockMvc.perform(get("/tickets/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Ticket Not Found"))
                .andExpect(jsonPath("$.detail").value("Could not find ticket with id: 42"));
    }

    @Test
    void create_withValidPayload_returnsCreated() throws Exception {
        Ticket created = new Ticket(5L, "API outage", "500 on /tickets", "OPEN");
        when(service.create(any(Ticket.class))).thenReturn(created);

        Ticket requestBody = new Ticket(null, "API outage", "500 on /tickets", "OPEN");
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("API outage"));
    }

    @Test
    void create_withInvalidStatus_returnsValidationProblemDetail() throws Exception {
        String payload = """
                {
                  "title": "Bad status",
                  "description": "Invalid value",
                  "status": "DONE"
                }
                """;

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.errors.status")
                        .value("Status must be OPEN, IN_PROGRESS, or CLOSED"));
    }

    @Test
    void create_withMalformedJson_returnsBadRequestProblemDetail() throws Exception {
        String payload = """
                {
                  "title": "Bad json",
                  "description": "Missing quote",
                  "status": OPEN
                }
                """;

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Request Body"));
    }

    @Test
    void update_whenMissing_returnsNotFound() throws Exception {
        when(service.update(any(Long.class), any(Ticket.class)))
                .thenThrow(new TicketNotFoundException(8L));

        Ticket requestBody = new Ticket(null, "Update", "Update desc", "IN_PROGRESS");
        mockMvc.perform(put("/tickets/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Ticket Not Found"));
    }

    @Test
    void delete_whenExists_returnsNoContent() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/tickets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenMissing_returnsNotFound() throws Exception {
        doThrow(new TicketNotFoundException(999L)).when(service).delete(999L);

        mockMvc.perform(delete("/tickets/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Could not find ticket with id: 999"));
    }
}

package com.michael.support_ticket_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repo;

    @InjectMocks
    private TicketService service;

    private Ticket existingTicket;

    @BeforeEach
    void setUp() {
        existingTicket = new Ticket(1L, "Login Issue", "Cannot login with SSO", "OPEN");
    }

    @Test
    void getAll_returnsAllTickets() {
        when(repo.findAll()).thenReturn(List.of(existingTicket));

        List<Ticket> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Login Issue");
    }

    @Test
    void getById_whenTicketExists_returnsTicket() {
        when(repo.findById(1L)).thenReturn(Optional.of(existingTicket));

        Ticket result = service.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo("OPEN");
    }

    @Test
    void getById_whenTicketMissing_throwsNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessage("Could not find ticket with id: 99");
    }

    @Test
    void create_savesAndReturnsTicket() {
        Ticket newTicket = new Ticket(null, "Payment Issue", "Card declined", "OPEN");
        Ticket savedTicket = new Ticket(2L, "Payment Issue", "Card declined", "OPEN");
        when(repo.save(newTicket)).thenReturn(savedTicket);

        Ticket result = service.create(newTicket);

        assertThat(result.getId()).isEqualTo(2L);
        verify(repo).save(newTicket);
    }

    @Test
    void update_whenTicketExists_updatesFieldsAndSaves() {
        Ticket updatePayload = new Ticket(null, "Updated title", "Updated description", "IN_PROGRESS");
        Ticket updatedTicket = new Ticket(1L, "Updated title", "Updated description", "IN_PROGRESS");

        when(repo.findById(1L)).thenReturn(Optional.of(existingTicket));
        when(repo.save(any(Ticket.class))).thenReturn(updatedTicket);

        Ticket result = service.update(1L, updatePayload);

        assertThat(result.getTitle()).isEqualTo("Updated title");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        assertThat(result.getStatus()).isEqualTo("IN_PROGRESS");
        verify(repo).save(existingTicket);
    }

    @Test
    void delete_whenTicketExists_deletesTicket() {
        when(repo.findById(1L)).thenReturn(Optional.of(existingTicket));

        service.delete(1L);

        verify(repo).delete(existingTicket);
    }

    @Test
    void delete_whenTicketMissing_throwsNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessage("Could not find ticket with id: 1");
    }
}

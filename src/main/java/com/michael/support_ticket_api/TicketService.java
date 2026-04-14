package com.michael.support_ticket_api;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repo;

    public TicketService(TicketRepository repo) {
        this.repo = repo;
    }

    public List<Ticket> getAll() {
        return repo.findAll();
    }

    public Ticket create(Ticket ticket) {
        return repo.save(ticket);
    }
}
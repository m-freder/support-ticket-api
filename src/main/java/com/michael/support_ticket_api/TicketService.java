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

    public Ticket getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

    public Ticket create(Ticket ticket) {
        return repo.save(ticket);
    }

    public Ticket update(Long id, Ticket updatedTicket) {
        Ticket existing = repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));

        existing.setTitle(updatedTicket.getTitle());
        existing.setDescription(updatedTicket.getDescription());
        existing.setStatus(updatedTicket.getStatus());

        return repo.save(existing);
    }

    public void delete(Long id) {
        Ticket existing = repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));

        repo.delete(existing);
    }
}

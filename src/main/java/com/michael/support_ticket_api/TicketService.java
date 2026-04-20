package com.michael.support_ticket_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository repo;

    public TicketService(TicketRepository repo) {
        this.repo = repo;
    }

    public List<Ticket> getAll() {
        List<Ticket> tickets = repo.findAll();
        log.info("Retrieved {} ticket(s)", tickets.size());
        return tickets;
    }

    public Ticket getById(Long id) {
        log.info("Fetching ticket id={}", id);
        return repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

    public Ticket create(Ticket ticket) {
        Ticket created = repo.save(ticket);
        log.info("Created ticket id={}", created.getId());
        return created;
    }

    public Ticket update(Long id, Ticket updatedTicket) {
        log.info("Updating ticket id={}", id);
        Ticket existing = repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));

        existing.setTitle(updatedTicket.getTitle());
        existing.setDescription(updatedTicket.getDescription());
        existing.setStatus(updatedTicket.getStatus());

        Ticket saved = repo.save(existing);
        log.info("Updated ticket id={}", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.info("Deleting ticket id={}", id);
        Ticket existing = repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));

        repo.delete(existing);
        log.info("Deleted ticket id={}", id);
    }
}

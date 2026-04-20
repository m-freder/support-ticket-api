package com.michael.support_ticket_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private static final Logger log = LoggerFactory.getLogger(TicketController.class);

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ticket> getAll() {
        log.info("Request received: list all tickets");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Ticket getById(@PathVariable Long id) {
        log.info("Request received: get ticket by id={}", id);
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket create(@Valid @RequestBody Ticket ticket) {
        log.info("Request received: create ticket");
        return service.create(ticket);
    }

    @PutMapping("/{id}")
    public Ticket update(@PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        log.info("Request received: update ticket id={}", id);
        return service.update(id, ticket);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Request received: delete ticket id={}", id);
        service.delete(id);
    }
}

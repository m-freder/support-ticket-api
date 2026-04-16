package com.michael.support_ticket_api;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("Could not find ticket with id: " + id);
    }
}

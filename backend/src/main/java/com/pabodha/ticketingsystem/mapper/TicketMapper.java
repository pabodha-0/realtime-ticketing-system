package com.pabodha.ticketingsystem.mapper;

import com.pabodha.ticketingsystem.model.Ticket;
import com.pabodha.ticketingsystem.runtime.TicketThread;

public class TicketMapper {
    Ticket mapToEntity(TicketThread ticketThread) {
        if (ticketThread == null) {
            return null;
        }

        Ticket ticket = new Ticket();

        ticket.setTicketId(ticketThread.getTicketId());
        ticket.setTicketPrice(ticketThread.getTicketPrice());
        ticket.setEventName(ticketThread.getEventName());

        return ticket;
    }
}

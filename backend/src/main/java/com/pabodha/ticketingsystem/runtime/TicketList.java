package com.pabodha.ticketingsystem.runtime;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Component
public class TicketList {
    private final List<TicketThread> tickets = Collections.synchronizedList(new ArrayList<>());

    public void addTicket(TicketThread ticket) {
        tickets.add(ticket);
    }

    public TicketThread removeTicket() {
        return tickets.remove(0);
    }

    public int getSize() {
        return tickets.size();
    }
}

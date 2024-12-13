package com.pabodha.ticketingsystem.runtime;

import com.pabodha.ticketingsystem.service.LogMessage;
import com.pabodha.ticketingsystem.service.LogService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class TicketPool {
//    private final List<TicketThread> ticketThreadList = Collections.synchronizedList(new ArrayList<>());
//    private final AtomicInteger currentTotalTickets = new AtomicInteger(0);
    private int maxCapacity;
    private int totalTickets;
    private AtomicInteger lastTicketId = new AtomicInteger(1);
    private final Date date = new Date();
    private final LogService logService;
    private final TicketList ticketList;

    public TicketPool(int maxCapacity, int totalTickets, LogService logService, TicketList ticketList) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.logService = logService;
        this.ticketList = ticketList;
    }

    public synchronized void addTicket(TicketThread ticketThread) {
        if(ticketList.getSize() >= totalTickets) {
            Thread.currentThread().interrupt();
        }

        while (ticketList.getSize() >= maxCapacity ) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("Error occurred while adding ticket to the pool");
            }
        }

        lastTicketId.incrementAndGet();
        ticketList.addTicket(ticketThread);
        notifyAll();
        logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(), "Ticket added to the pool: " + ticketThread));
//        System.out.println("Ticket added to the pool: " + ticketThread);
    }

    public synchronized TicketThread removeTicket() {
        if(ticketList.getSize() >= totalTickets) {
            Thread.currentThread().interrupt();
        }

        while (ticketList.getSize() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("Error occurred while retrieving ticket from the pool");
            }
        }

        TicketThread boughtTicketThread = ticketList.removeTicket();
        notifyAll();
        logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(), "Ticket removed from the pool: " + boughtTicketThread));
//        System.out.println("Ticket removed from the pool: " + boughtTicketThread);
        return boughtTicketThread;
    }
}

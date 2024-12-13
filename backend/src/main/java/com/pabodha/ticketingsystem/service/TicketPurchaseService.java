package com.pabodha.ticketingsystem.service;

import com.pabodha.ticketingsystem.model.Customer;
import com.pabodha.ticketingsystem.model.Ticket;
import com.pabodha.ticketingsystem.model.TicketPurchase;
import com.pabodha.ticketingsystem.repository.CustomerRepository;
import com.pabodha.ticketingsystem.repository.TicketPurchaseRepository;
import com.pabodha.ticketingsystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketPurchaseService {
    private final TicketPurchaseRepository ticketPurchaseRepository;
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;

    public TicketPurchaseService(TicketPurchaseRepository ticketPurchaseRepository, TicketRepository ticketRepository, CustomerRepository customerRepository) {
        this.ticketPurchaseRepository = ticketPurchaseRepository;
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
    }

    public List<TicketPurchase> getAllTicketPurchases() {
        return ticketPurchaseRepository.findAll();
    }

    public TicketPurchase createTicketPurchase(TicketPurchase ticketPurchase) throws InterruptedException {
        while(true) {
            Optional<Ticket> ticket = ticketRepository.findById(ticketPurchase.getTicket().getTicketId());
            Optional<Customer> customer = customerRepository.findById(ticketPurchase.getCustomer().getCustomerId());

            if (ticket.isPresent() && customer.isPresent()) {
                return ticketPurchaseRepository.save(ticketPurchase);
            }
        Thread.sleep(100);
        }
    }
}

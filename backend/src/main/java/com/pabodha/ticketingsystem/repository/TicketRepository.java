package com.pabodha.ticketingsystem.repository;

import com.pabodha.ticketingsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {}

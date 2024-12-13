package com.pabodha.ticketingsystem.repository;

import com.pabodha.ticketingsystem.model.TicketPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketPurchaseRepository extends JpaRepository<TicketPurchase, Integer> {}

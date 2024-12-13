package com.pabodha.ticketingsystem.repository;

import com.pabodha.ticketingsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {}

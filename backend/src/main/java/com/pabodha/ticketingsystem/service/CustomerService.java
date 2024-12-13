package com.pabodha.ticketingsystem.service;

import com.pabodha.ticketingsystem.model.Customer;
import com.pabodha.ticketingsystem.repository.CustomerRepository;
import com.pabodha.ticketingsystem.runtime.TicketSystemManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final TicketSystemManager ticketSystemManager;

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository, TicketSystemManager ticketSystemManager) {
        this.customerRepository = customerRepository;
        this.ticketSystemManager = ticketSystemManager;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        ticketSystemManager.startCustomer(customer.getCustomerId(), customer.getCustomerName(), customer.getRetrievalInterval());
        return savedCustomer;
    }
}

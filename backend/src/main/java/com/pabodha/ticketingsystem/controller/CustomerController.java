package com.pabodha.ticketingsystem.controller;

import com.pabodha.ticketingsystem.model.Customer;
import com.pabodha.ticketingsystem.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    public String getCustomer(@RequestBody Customer customer) {
        customerService.createCustomer(customer);
        return "Customer Created";
    }
}

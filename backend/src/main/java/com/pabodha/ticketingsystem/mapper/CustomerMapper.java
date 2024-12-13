package com.pabodha.ticketingsystem.mapper;

import com.pabodha.ticketingsystem.model.Customer;
import com.pabodha.ticketingsystem.runtime.CustomerThread;

public class CustomerMapper {
    Customer mapToEntity(CustomerThread customerThread) {
        if (customerThread == null) {
            return null;
        }

        Customer customer = new Customer();

        customer.setCustomerId(customerThread.getCustomerId());
        customer.setRetrievalInterval(customerThread.getRetrievalInterval());

        return customer;
    }
}

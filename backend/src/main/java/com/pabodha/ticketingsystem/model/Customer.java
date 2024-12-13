package com.pabodha.ticketingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int customerId;

    private String customerName;

    private int retrievalInterval;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<TicketPurchase> ticketPurchases;
}

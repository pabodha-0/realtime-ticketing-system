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
public class Vendor {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int vendorId;

    private String vendorName;

    private int ticketsPerRelease;

    private int releaseInterval;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    private List<Ticket> tickets;

}

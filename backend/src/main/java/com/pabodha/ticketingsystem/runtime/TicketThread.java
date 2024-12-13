package com.pabodha.ticketingsystem.runtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TicketThread {
    private int ticketId;
    private String eventName;
    private String ticketPrice;
}

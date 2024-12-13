package com.pabodha.ticketingsystem.service;

import com.pabodha.ticketingsystem.config.ConfigModel;
import com.pabodha.ticketingsystem.runtime.TicketSystemManager;
import org.springframework.stereotype.Service;

@Service
public class TicketSystemService {
    private final TicketSystemManager ticketSystemManager;

    public TicketSystemService(TicketSystemManager ticketSystemManager) {
        this.ticketSystemManager = ticketSystemManager;
    }

    public String setConfig(ConfigModel configModel) {
        ticketSystemManager.setConfig(configModel);
        return "Configuration updated";
    }

    public String start() {
		ticketSystemManager.startTicketSystem();
        return "Ticket System Started";
    }

    public String stop() {
        ticketSystemManager.stopSystem();
        return "Ticket System Stopped";
    }

    public boolean getStatus() {
        return ticketSystemManager.getStatus();
    }
}

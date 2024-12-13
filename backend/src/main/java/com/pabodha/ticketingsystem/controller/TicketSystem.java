package com.pabodha.ticketingsystem.controller;

import com.google.gson.Gson;
import com.pabodha.ticketingsystem.config.ConfigModel;
import com.pabodha.ticketingsystem.runtime.TicketList;
import com.pabodha.ticketingsystem.service.LogMessage;
import com.pabodha.ticketingsystem.service.LogService;
import com.pabodha.ticketingsystem.service.TicketSystemService;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@RestController
@CrossOrigin
@RequestMapping("/ticket-system")
public class TicketSystem {
    private final TicketSystemService ticketSystemService;
    private final TicketList ticketList;

    public TicketSystem(TicketSystemService ticketSystemService, TicketList ticketList) {
        this.ticketSystemService = ticketSystemService;
        this.ticketList = ticketList;
    }

    @PostMapping("/configure")
    public String configure(@RequestBody ConfigModel configModel) {
        return ticketSystemService.setConfig(configModel);
    }

    @GetMapping("/start")
    public String start() {
        ticketSystemService.start();
        return "Started Ticket System";
    }

    @GetMapping("/stop")
    public String stop() {
        ticketSystemService.stop();
        return "Stopped Ticket System";
    }

    @GetMapping("/status")
    public boolean status() {
        return ticketSystemService.getStatus();
    }

    @GetMapping("/current-tickets")
    public int getCurrentTickets() {
        return ticketList.getSize();
    }

}
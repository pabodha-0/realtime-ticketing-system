package com.pabodha.ticketingsystem.controller;

import com.pabodha.ticketingsystem.service.LogMessage;
import com.pabodha.ticketingsystem.service.LogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@CrossOrigin
public class LogController {
    private final LogService logService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    public CompletableFuture<List<LogMessage>> getLogs() {
        return logService.waitForLogs();
    }

    // For simulation: endpoint to add logs
    @GetMapping("/simulate-log")
    public void simulateLog() {
        executorService.execute(() -> logService.addLog(new LogMessage("INFO", "2021-09-01 12:00:00", "This is a log message")));
    }
}


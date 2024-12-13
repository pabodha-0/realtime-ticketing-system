package com.pabodha.ticketingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class LogService {
    private final List<LogMessage> logs = new ArrayList<>();
    private final List<CompletableFuture<List<LogMessage>>> listeners = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(LogService.class);

    public synchronized void addLog(LogMessage log) {
        logs.add(log);

        if (Objects.equals(log.getSeverity(), "ERROR")) {
            logger.error(log.getMessage());
        } else if (Objects.equals(log.getSeverity(), "INFO")) {
            logger.info(log.getMessage());
        } else if (Objects.equals(log.getSeverity(), "WARN")) {
            logger.warn(log.getMessage());
        }

        notifyListeners();
    }

    public synchronized CompletableFuture<List<LogMessage>> waitForLogs() {
        CompletableFuture<List<LogMessage>> future = new CompletableFuture<>();
        listeners.add(future);

        return future;
    }

    private void notifyListeners() {
        for (CompletableFuture<List<LogMessage>> listener : listeners) {
            listener.complete(new ArrayList<>(logs));
        }
        listeners.clear();
    }
}


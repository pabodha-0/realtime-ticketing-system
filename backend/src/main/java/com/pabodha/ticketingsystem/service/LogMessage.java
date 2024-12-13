package com.pabodha.ticketingsystem.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogMessage {
    private int logId;
    private String severity;
    private String timestamp;
    private String message;
    private static int logCounter = 0;

    public LogMessage(String severity, String timestamp, String message) {
        this.logId = ++logCounter;
        this.severity = severity;
        this.timestamp = timestamp;
        this.message = message;
    }
}

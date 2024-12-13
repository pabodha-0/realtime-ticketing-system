package com.pabodha.ticketingsystem.runtime;

import com.pabodha.ticketingsystem.model.Ticket;
import com.pabodha.ticketingsystem.model.Vendor;
import com.pabodha.ticketingsystem.service.LogMessage;
import com.pabodha.ticketingsystem.service.LogService;
import com.pabodha.ticketingsystem.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class VendorThread implements Runnable {
    private int vendorId;
    private int ticketsPerRelease;
    private int releaseInterval; // in seconds
    private TicketPool ticketPool;
    private TicketService ticketService;
    private LogService logService;
    private volatile boolean running;

    public VendorThread(int vendorId, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool, TicketService ticketService, LogService logService) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;
        this.ticketService = ticketService;
        this.logService = logService;
        this.running = true;
    }

    @Override
    public void run() {
        Date date = new Date();

        while (running) { // Check running flag
            try {
                // Generate ticket details
                int ticketId = ticketPool.getLastTicketId().intValue();
                TicketThread ticketThread = new TicketThread(ticketId, "Event " + ticketId, "LKR 1000");
                ticketPool.addTicket(ticketThread);

                // Create ticket object
                Ticket ticket = new Ticket();
                ticket.setTicketId(ticketId);
                ticket.setEventName("Event " + ticketId);
                ticket.setTicketPrice("LKR 1000");

                // Assign vendor to ticket
                Vendor vendor = new Vendor();
                vendor.setVendorId(vendorId);
                ticket.setVendor(vendor);

                // Persist ticket via service
                ticketService.createTicket(ticket);

                // Log success
                logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(),
                        "Vendor " + vendorId + " released ticket: " + ticketId));

                // Wait for the next release interval
                Thread.sleep(releaseInterval * 1000L);

            } catch (InterruptedException e) {
                // Handle thread interruption
                Thread.currentThread().interrupt(); // Restore interrupted status
                logService.addLog(new LogMessage("WARN", new Timestamp(date.getTime()).toString(),
                        "Vendor " + vendorId + " thread interrupted."));
                break;
            } catch (Exception e) {
                // Handle unexpected exceptions
                logService.addLog(new LogMessage("ERROR", new Timestamp(date.getTime()).toString(),
                        "Error occurred while releasing tickets for Vendor " + vendorId + ": " + e.getMessage()));
            }
        }

        logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(),
                "Vendor " + vendorId + " thread stopped."));
    }

    // Method to stop the thread gracefully
    public void stop() {
        this.running = false;
    }
}

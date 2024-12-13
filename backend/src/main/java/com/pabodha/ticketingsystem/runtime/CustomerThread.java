package com.pabodha.ticketingsystem.runtime;

import com.pabodha.ticketingsystem.model.Customer;
import com.pabodha.ticketingsystem.model.Ticket;
import com.pabodha.ticketingsystem.model.TicketPurchase;
import com.pabodha.ticketingsystem.service.LogMessage;
import com.pabodha.ticketingsystem.service.LogService;
import com.pabodha.ticketingsystem.service.TicketPurchaseService;
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
public class CustomerThread implements Runnable {
    private int customerId;
    private String customerName;
    private int retrievalInterval;
    private TicketPool ticketPool;
    private TicketPurchaseService ticketPurchaseService;
    private final LogService logService;
    private volatile boolean running;

    public CustomerThread(int customerId, String customerName, int retrievalInterval, TicketPool ticketPool, TicketPurchaseService ticketPurchaseService, LogService logService) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;
        this.ticketPurchaseService = ticketPurchaseService;
        this.logService = logService;
        this.running = true;
    }

    @Override
    public void run() {
        Date date = new Date();

        while (running) {
            try {
                // Remove ticket from the pool
                TicketThread ticketThread = ticketPool.removeTicket();
                if (ticketThread == null) {
                    logService.addLog(new LogMessage("WARN", new Timestamp(date.getTime()).toString(),
                            "No tickets available for Customer " + customerName));
                    Thread.sleep(retrievalInterval * 1000L); // Wait and retry
                    continue;
                }

                // Log successful retrieval
                logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(),
                        "Customer " + customerName + " retrieved ticket " + ticketThread.getEventName()));

                // Create ticket purchase record
                TicketPurchase ticketPurchase = new TicketPurchase();

                Customer customer = new Customer();
                customer.setCustomerId(customerId);

                Ticket ticket = new Ticket();
                ticket.setTicketId(ticketThread.getTicketId());

                ticketPurchase.setCustomer(customer);
                ticketPurchase.setTicket(ticket);

                ticketPurchaseService.createTicketPurchase(ticketPurchase);

                // Wait for the next retrieval
                Thread.sleep(retrievalInterval * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                logService.addLog(new LogMessage("WARN", new Timestamp(date.getTime()).toString(),
                        "Customer " + customerName + " thread interrupted."));
                break; // Exit the loop
            } catch (Exception e) {
                // Log unexpected errors
                logService.addLog(new LogMessage("ERROR", new Timestamp(date.getTime()).toString(),
                        "Error occurred while processing ticket for Customer " + customerName + ": " + e.getMessage()));
            }
        }

        logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(),
                "Customer " + customerName + " thread stopped."));
    }

    // Method to stop the thread gracefully
    public void stop() {
        this.running = false;
    }
}

package com.pabodha.ticketingsystem.runtime;

import com.google.gson.Gson;
import com.pabodha.ticketingsystem.config.ConfigModel;
import com.pabodha.ticketingsystem.model.Customer;
import com.pabodha.ticketingsystem.model.Vendor;
import com.pabodha.ticketingsystem.service.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Component
public class TicketSystemManager {
    private TicketPool ticketPool;
    private final TicketService ticketService;
    private final TicketPurchaseService ticketPurchaseService;
    private final LogService logService;
    private final Date date = new Date();

    private final List<VendorThread> vendorThreads = new ArrayList<>();
    private final List<CustomerThread> customerThreads = new ArrayList<>();
    private final TicketList ticketList;
    private volatile boolean running = false;
    private final Path filePath = Paths.get("config.json");
    private final Gson gson = new Gson();

    public TicketSystemManager(
            TicketService ticketService,
            TicketPurchaseService ticketPurchaseService,
            LogService logService,
            TicketList ticketList
    ) {
        this.ticketService = ticketService;
        this.ticketPurchaseService = ticketPurchaseService;
        this.logService = logService;
        this.ticketList = ticketList;
    }

    public void setConfig(ConfigModel config) {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(config, writer);
            logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(), "Configuration saved successfully!"));
        } catch (Exception e) {
            logService.addLog(new LogMessage("ERROR", new Timestamp(date.getTime()).toString(), "Error occurred while saving configuration"));
        }
    }

    public void startTicketSystem() {
        running = true;
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath.toFile())) {
            ConfigModel config = gson.fromJson(reader, ConfigModel.class);
            this.ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets(), logService, ticketList);
            logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(), "Ticketing system started."));
        } catch (IOException e) {
            logService.addLog(new LogMessage("ERROR", new Timestamp(date.getTime()).toString(), "Error occurred while reading configuration file"));
            e.printStackTrace();
        }
    }

    public void startVendor(int vendorId, String vendorName, int ticketReleaseRate) {
        VendorThread vendorThread = new VendorThread(vendorId, 2, ticketReleaseRate, ticketPool, ticketService, logService);
        Thread thread = new Thread(vendorThread, "Vendor: " + vendorName);
        vendorThreads.add(vendorThread);
        Vendor vendor = new Vendor();
        vendor.setVendorId(vendorId);
        vendor.setTicketsPerRelease(2);
        vendor.setReleaseInterval(ticketReleaseRate);

        thread.start();
    }

    public void startCustomer(int customerId, String customerName, int retrievalInterval) {
        CustomerThread customerThread = new CustomerThread(customerId, customerName, retrievalInterval, ticketPool, ticketPurchaseService, logService);
        Thread thread = new Thread(customerThread, "Customer: " + customerName);
        customerThreads.add(customerThread);
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName(customerName);
        customer.setRetrievalInterval(retrievalInterval);

        thread.start();
    }

    public void stopSystem() {
        logService.addLog(new LogMessage("INFO", new Timestamp(new Date().getTime()).toString(), "Stopping ticketing system..."));

        // Stop all vendor threads
        for (VendorThread vendorThread : vendorThreads) {
            vendorThread.stop();
        }

        // Stop all customer threads
        for (CustomerThread customerThread : customerThreads) {
            customerThread.stop();
        }
        running = false;
        logService.addLog(new LogMessage("INFO", new Timestamp(new Date().getTime()).toString(), "Ticketing system stopped."));
    }

    public boolean getStatus() {
        return running;
    }
}

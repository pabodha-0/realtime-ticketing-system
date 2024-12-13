package com.pabodha.ticketingsystem.config;

import com.google.gson.Gson;
import com.pabodha.ticketingsystem.model.Customer;
import com.pabodha.ticketingsystem.model.Vendor;
import com.pabodha.ticketingsystem.service.*;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

@Configuration
public class ConfigurationCLI {
    private final Scanner scanner = new Scanner(System.in);
    private final Gson gson = new Gson();
    private final ConfigModel configModel = new ConfigModel();
    private final Path filePath = Paths.get("config.json");
    private final CustomerService customerService;
    private final TicketSystemService ticketSystemService;
    private final VendorService vendorService;
    private final LogService logService;
    private final Date date = new Date();

    private boolean systemRunning = false; // Flag to indicate if the system is running

    public ConfigurationCLI(CustomerService customerService, TicketSystemService ticketSystemService, VendorService vendorService, LogService logService) {
        this.customerService = customerService;
        this.ticketSystemService = ticketSystemService;
        this.vendorService = vendorService;
        this.logService = logService;
    }

    public void start() {
        System.out.println("""
                -----------------------------------------
                Welcome to the Realtime Ticketing System!
                -----------------------------------------
                """);

        while (true) {
            if (!systemRunning) { // Only show the menu if the system is not running
                System.out.print("""
                        Select one of the following options,
                        1. Configure the ticketing system
                        2. Start the ticketing system
                        3. Exit
                        
                        Enter the the option number:
                        """);
            }

            int option = getPositiveInteger(scanner);

            switch (option) {
                case 1 -> configureSystem();
                case 2 -> {
                    if (!systemRunning) {
                        new Thread(this::startTicketSystem).start(); // Run in a separate thread
                    } else {
                        System.out.println("Ticketing system is already running!");
                    }
                }
                case 3 -> System.exit(0);
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void configureSystem() {
        System.out.println("Enter the following details to configure the ticketing system");

        System.out.print("Enter the total number of tickets: ");
        configModel.setTotalTickets(getPositiveInteger(scanner));

        System.out.print("Enter the ticket release rate: ");
        configModel.setTicketReleaseRate(getPositiveInteger(scanner));

        System.out.print("Enter Customer Retrieval Rate (tickets per second): ");
        configModel.setCustomerRetrievalRate(getPositiveInteger(scanner));

        System.out.print("Enter Maximum Ticket Capacity: ");
        configModel.setMaxTicketCapacity(getPositiveInteger(scanner));

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(configModel, writer);
//            logService.addLog("INFO", "Configuration saved successfully!");
            logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(), "Configuration saved successfully!"));
//            System.out.println("Configuration saved successfully!");
        } catch (Exception e) {
            logService.addLog(new LogMessage("ERROR", new Timestamp(date.getTime()).toString(), "Error occurred while saving configuration"));
//            logService.addLog("ERROR", "Error occurred while saving configuration");
//            System.out.println("Error occurred while saving configuration");
        }
    }

    public void startTicketSystem() {
        systemRunning = true; // Set flag to indicate system is running
        logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(), "Starting the ticketing system..."));
//        logService.addLog("INFO", "Starting the ticketing system...");
//        System.out.println("Starting the ticketing system...");

        ticketSystemService.start();

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath.toFile())) {
            ConfigModel config = gson.fromJson(reader, ConfigModel.class);

            for (int i = 0; i < 10; i++) {
                Customer customer = new Customer();
                customer.setCustomerId(i);
                customer.setCustomerName("Customer " + i);
                customer.setRetrievalInterval(config.getCustomerRetrievalRate());
                customerService.createCustomer(customer);
            }

            for (int i = 0; i < 10; i++) {
                Vendor vendor = new Vendor();
                vendor.setVendorId(i);
                vendor.setVendorName("Vendor " + i);
                vendor.setTicketsPerRelease(2);
                vendor.setReleaseInterval(config.getTicketReleaseRate());
                vendorService.createVendor(vendor);
            }

        } catch (IOException e) {
            e.printStackTrace();
            logService.addLog(new LogMessage("ERROR", new Timestamp(date.getTime()).toString(), "Error occurred while reading configuration file"));
//            logService.addLog("ERROR", "Error occurred while reading configuration file");
//            System.out.println("Error occurred while reading configuration file");
        }



//        System.out.println("Ticketing system is running. Press Ctrl+C to stop.");
        try {
            Thread.sleep(Long.MAX_VALUE); // Keep thread running indefinitely
        } catch (InterruptedException e) {
            logService.addLog(new LogMessage("INFO", new Timestamp(date.getTime()).toString(), "Ticketing system stopped."));
//            logService.addLog("INFO", "Ticketing system stopped.");
//            System.out.println("Ticketing system stopped.");
        } finally {
            systemRunning = false; // Reset flag when system stops
        }
    }

    private int getPositiveInteger(Scanner sc) {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine());
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}

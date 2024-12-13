package com.pabodha.ticketingsystem;

import com.pabodha.ticketingsystem.config.ConfigurationCLI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketingsystemApplication implements CommandLineRunner {
	private final ConfigurationCLI configurationCLI;

	public TicketingsystemApplication(ConfigurationCLI configurationCLI) {
		this.configurationCLI = configurationCLI;
	}

	public static void main(String[] args) {
		// Run the Spring Boot Application
		SpringApplication.run(TicketingsystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Run CLI to get configuration details
//		ConfigurationCLI configurationCLI = new ConfigurationCLI();
		configurationCLI.start();
	}
}

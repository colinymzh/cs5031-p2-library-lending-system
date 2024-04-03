package com.CS5031P2;

import com.CS5031P2.frontend.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * The entry point for the application.
 * This class initialises the Spring Boot application and launches the MainFrame class from the frontend package.
 */
@SpringBootApplication
@RestController
public class Application {
	/**
	 * The main method, entry point of the application.
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(Application.class, args);
		MainFrame.main(args);
	}
}

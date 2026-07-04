package com.automob.mechanic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutomobMechanicApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutomobMechanicApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  Automob-Mechanic Backend is RUNNING!");
        System.out.println("  Website   → http://localhost:8080");
        System.out.println("  Admin     → http://localhost:8080/admin");
        System.out.println("  API       → http://localhost:8080/api/bookings");
        System.out.println("  DB Console→ http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }
}

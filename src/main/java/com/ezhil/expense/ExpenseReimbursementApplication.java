package com.ezhil.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class ExpenseReimbursementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseReimbursementApplication.class, args);
	}

}

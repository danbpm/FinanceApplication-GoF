package org.tigerbank.finance.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.tigerbank.finance")
public class FinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }
}
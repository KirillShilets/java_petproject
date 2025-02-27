package org.myproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan("org.myproject.entity")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

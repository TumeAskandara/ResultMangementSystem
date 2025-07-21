package com.example.resultmanagementsystem;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResultManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResultManagementSystemApplication.class, args);

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.out.println("Mail username: " + dotenv.get("SPRING_MAIL_USERNAME"));
        System.out.println("Mail password: " + dotenv.get("SPRING_MAIL_PASSWORD"));
    }

}

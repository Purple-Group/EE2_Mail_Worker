package com.ee2.mail_worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class Ee2MailWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ee2MailWorkerApplication.class, args);
    }

}

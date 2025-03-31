package sfu.informationsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InformationSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(InformationSecurityApplication.class, args);
    }
}

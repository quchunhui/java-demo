package rexel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MindSphereApplication {
    public static void main(String[] args) {
        SpringApplication.run(MindSphereApplication.class, args);
    }
}
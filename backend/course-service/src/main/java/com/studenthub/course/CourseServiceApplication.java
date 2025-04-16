package com.studenthub.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Uygulamanın ana sınıfıdır ve Spring Boot uygulamasının başlatılmasını sağlar.
 * Bu sınıf, Spring Boot'un başlatılmasını ve yapılandırılmasını yönetir.
 */
@SpringBootApplication // Spring Boot uygulamasının başlangıç sınıfı olarak işaret eder
public class CourseServiceApplication {

    /**
     * Uygulamanın çalışmaya başladığı ana metottur.
     * Spring Boot uygulamasını başlatır.
     * 
     * @param args Komut satırından gelen argümanlar.
     */
    public static void main(String[] args) {
        // Spring Boot uygulamasını başlatır
        SpringApplication.run(CourseServiceApplication.class, args);
    }
}

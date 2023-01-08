package com.example.role;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RoleApplication {
    @GetMapping("/")
    public String hell(HttpServletRequest rq){
        System.out.println(rq.getRemoteHost());
        return "<p style=\"font-size:50px\">hello spring boot 3.0</p>";
    }
    public static void main(String[] args) {
        SpringApplication.run(RoleApplication.class, args);
    }

}

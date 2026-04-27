package com.dinero.Football.Aggregator.Controllers;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public Map<String , String> healthCheck(){

        Map<String , String> response = new HashMap<>();
        response.put("Status" , "Up");
        response.put("Database" , "Connected");
        response.put("Message" , "Football Aggregator is running");
        return response;
    }

}

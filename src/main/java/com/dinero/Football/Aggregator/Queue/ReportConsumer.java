package com.dinero.Football.Aggregator.Queue;


import com.dinero.Football.Aggregator.Client.FootballApiClient;
import com.dinero.Football.Aggregator.Config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ReportConsumer {

    @Autowired
    private FootballApiClient footballApiClient;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void processReportJob(String matchId) {
        System.out.println("Processing report for match: " + matchId);

        try {
            String content = footballApiClient.getMatchById(matchId).block();
            Files.createDirectories(Path.of("reports"));
            Files.writeString(Path.of("reports/" + matchId + ".json"), content);
            System.out.println("Report saved for match: " + matchId);
        } catch (Exception e) {
            System.out.println("Failed to generate report: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

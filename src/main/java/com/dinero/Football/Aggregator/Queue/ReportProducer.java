package com.dinero.Football.Aggregator.Queue;


import com.dinero.Football.Aggregator.Config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishReportJob(String matchId){

        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME , "report.key" , matchId);

    }

}

package com.dinero.Football.Aggregator.Config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "report.queue";
    public static final String EXCHANGE_NAME = "report.exchange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue , DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("report.key");
    }
}

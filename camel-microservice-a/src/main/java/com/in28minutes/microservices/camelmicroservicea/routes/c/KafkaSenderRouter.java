package com.in28minutes.microservices.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class KafkaSenderRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // send from json directory to kafka

        from("file:camel-microservice-a/files/json")
                .log("${body}")
                .to("kafka:my-kafka-topic");
    }
}

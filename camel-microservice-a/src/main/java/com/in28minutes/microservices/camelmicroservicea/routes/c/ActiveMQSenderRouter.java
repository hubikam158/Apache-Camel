package com.in28minutes.microservices.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMQSenderRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        // send from timer endpoint to queue

//        from("timer:active-mq-timer?period=10000")
//                .transform().constant("My message for ActiveMQ")
//                .log("${body}")
//                .to("activemq:my-activemq-queue");

//        // send from json directory to queue
//
//        from("file:camel-microservice-a/files/json")
//                .log("${body}")
//                .to("activemq:my-activemq-queue");

        // send from xml directory to queue

        from("file:camel-microservice-a/files/xml")
                .log("${body}")
                .to("activemq:my-activemq-xml-queue");
    }
}

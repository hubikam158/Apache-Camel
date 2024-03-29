package com.in28minutes.microservices.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RestApiConsumerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // call rest api

        restConfiguration().host("localhost").port(8000);

        from("timer:rest-api-consumer?period=30000")
                .setHeader("from", () -> "EUR")
                .setHeader("to", () -> "PLN")
                .log("${body}")
                .to("rest:get:/currency-exchange/from/{from}/to/{to}")
                .log("${body}");
    }
}

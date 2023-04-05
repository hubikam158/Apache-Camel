package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import com.in28minutes.microservices.camelmicroservicea.utils.ArrayListAggregationStrategy;
import com.in28minutes.microservices.camelmicroservicea.utils.CurrencyExchange;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class EipPatternsRouter extends RouteBuilder {

    @Autowired
    private SplitterComponent splitter;

    @Autowired
    private DynamicRouterBean dynamicRouterBean;

    @Override
    public void configure() throws Exception {

//        getContext().setTracing(true); // gives us a lot of detailed information
//
//        // send the failed messages to the specified queue, to be sure that there will be no messages lost
//        errorHandler(deadLetterChannel("activemq:dead-letter-queue"));

        // pipeline pattern - example in MyFilesRouter
        // content based routing pattern - example in MyFilesRouter

//        // multicast pattern - allows us to specify multiple endpoints
//        from("timer:multicast?period=50000")
//                .multicast()
//                .to("log:sth1","log:sth2","log:sth3");

//        // split pattern - allows us to split body of csv file
//        from("file:camel-microservice-a/files/csv")
//                .unmarshal().csv()
//                .split(body())
//                .to("activemq:split-queue")
//                .to("log:split-files");

//        // split pattern - using delimiter
//        from("file:camel-microservice-a/files/csv")
//                .convertBodyTo(String.class)
//                .split(body(),",")
//                .to("activemq:split-queue");

//        // split pattern - using Splitter Component
//        from("file:camel-microservice-a/files/csv")
//                .convertBodyTo(String.class)
//                .split(method(splitter))
//                .to("activemq:split-queue");

//        // aggregate pattern - aggregating numerous messages into one
//        from("file:camel-microservice-a/files/aggregate-json")
//                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
//                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
//                // {body.to} groups all the messages that have the same value for parameter "to" and aggregates them
//                // if we will have few different values of "to", f.ex. IRN, PLN, USD, they will be aggregated separately
//                .completionSize(3)
//                .completionTimeout(10000)
//                .to("log:aggregate-json");

//        // routing slip pattern - pattern similar to multicast, but endpoints situated dynamically as String (Simple)
//        String routingSlip = "direct:endpoint1,direct:endpoint3";
//        from("timer:routing-slip?period=20000")
//                .transform().constant("My hardcoded message")
//                .routingSlip(simple(routingSlip));
//
//        from("direct:endpoint1")
//                .to("log:direct-endpoint1");
//        from("direct:endpoint2")
//                .to("log:direct-endpoint2");
//        from("direct:endpoint3")
//                .to("log:direct-endpoint3");

        // dynamic routing pattern - pattern similar to routing slip, but endpoints are chosen dynamically and in loop
        from("timer:dynamic-routing?period={{time-period}}")
                .transform().constant("My hardcoded message")
                .dynamicRouter(method(dynamicRouterBean));

        from("direct:endpoint1")
                .wireTap("log:wire-tap") // saving everything what is processed in the route to the specified endpoint
                .to("{{endpoint-1-for-logging}}");
        from("direct:endpoint2")
                .to("log:direct-endpoint2");
        from("direct:endpoint3")
                .to("log:direct-endpoint3");
    }
}

@Component
class SplitterComponent {
    public List<String> splitInput(String body) {
        return Collections.unmodifiableList(Arrays.asList("ABC", "DEF", "GHI"));
    }
}

@Component
class DynamicRouterBean{

    Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);
    int invocation = 0;

    public String decideTheNextEndpoint(@Body String body,
                                        @Headers Map<String,String> headers,
                                        @ExchangeProperties Map<String,String> properties) {

        logger.info("Dynamic Router Bean: {} {} {}", body,headers,properties);
        invocation++;

        if (invocation % 3 == 0) {
            return "direct:endpoint1";
        } else if (invocation % 3 == 1) {
            return "direct:endpoint2,direct:endpoint3";
        }
        return null;
    }
}
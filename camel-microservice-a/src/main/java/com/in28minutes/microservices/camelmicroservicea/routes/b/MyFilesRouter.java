package com.in28minutes.microservices.camelmicroservicea.routes.b;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class MyFilesRouter extends RouteBuilder {

    @Autowired
    private DeciderBean deciderBean;

    @Override
    public void configure() throws Exception {

        from("file:camel-microservice-a/files/input")
                .pipeline() // optional command, because pipeline is the default pattern
                .routeId("Files-Input-Route")
                .transform().body(String.class)
                .choice() // content based routing pattern
                .when(simple("${file:ext} ends with 'xml'"))
                .log("XML FILE")
//                .when(simple("${body} contains 'USD'"))
                .when(method(deciderBean))
                .log("Not an XML FILE but contains USD")
                .otherwise()
                .log("Not an XML FILE")
                .end()
//                .to("direct://log-file-values")
                .to("file:camel-microservice-a/files/output");

        from("direct:log-file-values")
                .log("${messageHistory} ${headers.CamelFileLastModified}")
                .log("${file:absolute.path}")
                .log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
                .log("${file:onlyname.noext} ${file:parent} ${file:path} ${file:absolute}")
                .log("${file:size} ${file:modified}")
                .log("${routeId} ${camelId} ${body}");
    }
}

@Component
class DeciderBean{

    Logger logger = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isConditionMet(@Body String body,
                                  @Headers Map<String,String> headers,
                                  @ExchangeProperties Map<String,String> exchangeProperties) {

        logger.info("Decider Bean: {} {} {}", body,headers,exchangeProperties);
        return true;
    }
}
package com.in28minutes.microservices.camelmicroserviceb.routes;

import com.in28minutes.microservices.camelmicroserviceb.utils.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ActiveMQReceiverRouter extends RouteBuilder {

    @Autowired
    private MyCurrencyExchangeProcessor myCurrencyExchangeProcessor;

    @Autowired
    private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;

    @Override
    public void configure() throws Exception {

//        // JSON -> CurrencyExchange
//        // { "id": 1000, "from": "USD", "to": "INR", "conversionMultiple": 70}
//        from("activemq:my-activemq-queue")
//                .unmarshal()
//                .json(JsonLibrary.Jackson, CurrencyExchange.class)
//                .bean(myCurrencyExchangeProcessor)
//                .bean(myCurrencyExchangeTransformer)
//                .to("log:received-message-from-activemq");

//        // XML -> CurrencyExchange
//        from("activemq:my-activemq-xml-queue")
//                .unmarshal()
//                .jacksonxml(CurrencyExchange.class)
//                .to("log:received-message-from-activemq");

        // CSV -> split log
        from("activemq:split-queue")
                .to("log:received-message-from-activemq");
    }
}
@Component
class MyCurrencyExchangeProcessor {

    Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);

    public void processMessage(CurrencyExchange currencyExchange) {
        logger.info("Process value with conversion multiple: {}",currencyExchange.getConversionMultiple());
    }
}

@Component
class MyCurrencyExchangeTransformer {

    Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);

    public CurrencyExchange transformMessage(CurrencyExchange currencyExchange) {
        currencyExchange.setConversionMultiple(
                currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));

        logger.info("New conversion multiple: {}",currencyExchange.getConversionMultiple());
        return currencyExchange;
    }
}

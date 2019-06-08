package spring.boot.apache.camel.saga.in.memory.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.saga.InMemorySagaService;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import spring.boot.apache.camel.saga.in.memory.processor.BankACreditProcessor;
import spring.boot.apache.camel.saga.in.memory.processor.BankADebitProcessor;
import spring.boot.apache.camel.saga.in.memory.processor.BankBCreditProcessor;
import spring.boot.apache.camel.saga.in.memory.processor.BankBDebitProcessor;
import spring.boot.apache.camel.saga.in.memory.service.AccountBankAService;

import java.util.Random;

@Configuration
public class CamelConfiguration extends RouteBuilder {

    @Autowired
    CamelContext camelContext;

    @Autowired
    AccountBankAService accountBankAService;

    @Autowired
    BankADebitProcessor bankADebitProcessor;

    @Autowired
    BankACreditProcessor bankACreditProcessor;

    @Autowired
    BankBDebitProcessor bankBDebitProcessor;

    @Autowired
    BankBCreditProcessor bankBCreditProcessor;

    @Override
    public void configure() throws Exception {

        camelContext.addService(new InMemorySagaService());
        restConfiguration().port(8381).host("localhost");

        from("direct:transfer")
                .removeHeaders("CamelHttp*")
                .saga()
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setHeader("id", new Random(System.currentTimeMillis()).nextInt(7) + 1);
                        exchange.getIn().setHeader("amount", new Random(System.currentTimeMillis()).nextInt(100000) + 1);
                    }
                })
                .log("############################ Id= ${header[id]}")
                .log("############################ Amount= ${header[amount]}")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .log("############################ Executing saga #${header.id}")
                .multicast()
                .parallelProcessing()
                .to("http4://localhost:8381/bank-a")
                .to("http4://localhost:8381/bank-b");



        rest().post("/bank-a")
                .param().type(RestParamType.header).name("id").required(true).endParam()
                .param().type(RestParamType.header).name("amount").required(true).endParam()
                .route()
                .saga()
                    .propagation(SagaPropagation.MANDATORY)
                    .option("id", header("id"))
                    .option("amount", header("amount"))
                    .compensation("direct:cancelDebit")
                .log("############################ Debit ${header.amount}$ for account #${header.id} is going to be done...")
                .doTry()
                    .process(bankADebitProcessor)
                .doCatch(IllegalArgumentException.class, IllegalStateException.class)
                    .log("############################ " + exceptionMessage().toString())
                    .throwException(new RuntimeException("############################ Debit for account #${header.id} failed"))
                .endDoTry()
                .log("############################ Debit for account #${header.id} done");
        from("direct:cancelDebit")
                .process(bankACreditProcessor)
                .log("############################ Debit for account #${header.id} has been cancelled");



        rest().post("/bank-b")
                .param().type(RestParamType.header).name("id").required(true).endParam()
                .param().type(RestParamType.header).name("amount").required(true).endParam()
                .route()
                .saga()
                    .propagation(SagaPropagation.MANDATORY)
                    .option("id", header("id"))
                    .option("amount", header("amount"))
                    .compensation("direct:cancelCredit")
                .log("############################ Credit ${header.amount}$ for account #${header.id} is going to be done...")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setHeader("random-x", new Random(System.currentTimeMillis()).nextInt(100));
                    }
                })
                .doTry()
                    .process(bankBCreditProcessor)
                .doCatch(IllegalStateException.class)
                    .log("############################ " + exceptionMessage().toString())
                    .throwException(new RuntimeException("############################ Credit for account #${header.id} failed"))
                .endDoTry()
                .log("############################ random-x = ${header[random-x]}")
                .choice()
                    .when(header("random-x").isGreaterThan(101))
                        .throwException(new RuntimeException("Random failure during payment"))
                .log("############################ Credit for account #${header.id} done");
        from("direct:cancelCredit")
                .process(bankBDebitProcessor)
                .log("############################ Credit for account #${header.id} has been cancelled");

        camelContext.start();
    }
}

package spring.boot.apache.camel.saga.in.memory.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.boot.apache.camel.saga.in.memory.service.AccountBankAService;

@Component
public class BankACreditProcessor implements Processor {

    @Autowired
    AccountBankAService accountBankAService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Long id = Long.valueOf(exchange.getIn().getHeader("id").toString());
        Long amount = Long.valueOf(exchange.getIn().getHeader("amount").toString());
        accountBankAService.increaseAmount(id, amount);
    }
}

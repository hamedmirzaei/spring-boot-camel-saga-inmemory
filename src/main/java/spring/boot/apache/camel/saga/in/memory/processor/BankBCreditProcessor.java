package spring.boot.apache.camel.saga.in.memory.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.boot.apache.camel.saga.in.memory.service.AccountBankBService;

@Component
public class BankBCreditProcessor implements Processor {

    @Autowired
    AccountBankBService accountBankBService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Long id = Long.valueOf(exchange.getIn().getHeader("id").toString());
        Long amount = Long.valueOf(exchange.getIn().getHeader("amount").toString());
        accountBankBService.increaseAmount(id, amount);
    }
}

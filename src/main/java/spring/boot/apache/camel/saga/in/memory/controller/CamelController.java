package spring.boot.apache.camel.saga.in.memory.controller;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.boot.apache.camel.saga.in.memory.service.AccountBankAService;
import spring.boot.apache.camel.saga.in.memory.service.AccountBankBService;

@Controller
public class CamelController {

    @Autowired
    CamelContext camelContext;

    @Autowired
    AccountBankAService accountBankAService;

    @Autowired
    AccountBankBService accountBankBService;

    @GetMapping("/transfer")
    public String transferMoney(Model model) {
        try {
            ProducerTemplate template = camelContext.createProducerTemplate();
            template.sendBody("direct:transfer", "This is bean example");
            model.addAttribute("msg", "Successful Saga");
        } catch (Exception e) {
            model.addAttribute("msg", "Failed Saga");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        model.addAttribute("bankAAccounts", accountBankAService.findAll());
        model.addAttribute("bankBAccounts", accountBankBService.findAll());

        return "index";
    }

}

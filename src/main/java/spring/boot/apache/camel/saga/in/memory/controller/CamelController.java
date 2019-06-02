package spring.boot.apache.camel.saga.in.memory.controller;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.saga.InMemorySagaService;
import org.apache.camel.util.jndi.JndiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CamelController {

    @Autowired
    CamelContext camelContext;

    @GetMapping("/transfer")
    public String transferMoney(Model model) {
        try {
            ProducerTemplate template = camelContext.createProducerTemplate();
            camelContext.start();
            template.sendBody("direct:transfer", "This is bean example");
            model.addAttribute("msg", "Success");
        } catch (Exception e) {
            model.addAttribute("msg", "Failure");
        } finally {
            try {
                //camelContext.stop();
            } catch (Exception ex) {
            }
        }

        return "index";
    }

}

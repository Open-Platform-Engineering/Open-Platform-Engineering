package codes.showme.server;

import codes.showme.domain.incident.Incident;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {

    @GetMapping("/")
    public String index() {
        Incident incident = new Incident();
        System.out.println(incident.getClass());
        return "index";
    }
}
package codes.showme.server.api;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EnqueueController {

    /**
     * https://developer.pagerduty.com/docs/ZG9jOjExMDI5NTgx-send-an-alert-event
     *
     * @return
     */
    @GetMapping("/v2/enqueue")
    public String enqueue(IncidentReq incidentReq) {
        return "hello";
    }
}

package codes.showme.server.schedule;

import codes.showme.domain.schedule.ScheduleRule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ScheduleController {

    public static final String URL_CREATE_SCHEDULE = "/v1/schedule";

    /**
     * create a new schedule
     * https://support.pagerduty.com/docs/schedule-basics#create-a-schedule
     */
    @PostMapping(URL_CREATE_SCHEDULE)
    public ResponseEntity<?> createScheduleStage1(@RequestBody CreateReq createReq) {
        ScheduleRule scheduleRule = createReq.convertToScheduleRule();
        long id = scheduleRule.save();
        return ResponseEntity.ok().header("id", id + "").build();
    }

}

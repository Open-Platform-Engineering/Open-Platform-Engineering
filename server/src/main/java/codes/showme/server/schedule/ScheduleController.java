package codes.showme.server.schedule;

import codes.showme.domain.account.Account;
import codes.showme.domain.schedule.ScheduleRule;
import codes.showme.server.auth.Auth;
import codes.showme.server.auth.Subject;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.pagination.Pagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ScheduleController {

    public static final String URL_CREATE_SCHEDULE = "/v1/schedule";
    public static final String URL_CREATE_SCHEDULE_SEARCH_USERS = "/v1/schedule/search/users";

    public static final String URL_LIST_SCHEDULES = "/v1/schedules";
    public static final String URL_ZONEIDS = "/v1/zoneids";
    public static final int SEARCH_USERS_LIMIT = 30;

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

    @GetMapping(URL_LIST_SCHEDULES)
    public ResponseEntity<Pagination<ScheduleRule>> list(@RequestParam(name = "page", defaultValue = "1") int pageNum, @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return ResponseEntity.ok().body(ScheduleRule.list(pageNum, limit));
    }

    @GetMapping(URL_ZONEIDS)
    public ResponseEntity<?> listAllZoneId() {
        return ResponseEntity.ok().body(ZoneId.getAvailableZoneIds());
    }

    /**
     * search users who can be added into the schedule
     * @return
     */
    @GetMapping(URL_CREATE_SCHEDULE_SEARCH_USERS)
    public ResponseEntity<?> listUserToSchedule(@RequestParam(name = "q",required = false) String q){
        Auth auth = InstanceFactory.getInstance(Auth.class);
        Subject subject = auth.getSubject();
        Account account = subject.getAccount();
        List<Account> members = account.findTeamMember(q, SEARCH_USERS_LIMIT);
        List<AccountTeamMember> result = AccountTeamMember.convert(members);
        return ResponseEntity.ok().body(result);
    }

}

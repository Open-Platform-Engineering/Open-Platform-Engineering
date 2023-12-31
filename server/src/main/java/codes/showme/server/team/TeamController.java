package codes.showme.server.team;

import codes.showme.domain.team.Team;
import codes.showme.techlib.pagination.Pagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class TeamController {

    public static final String URL_CREATE_TEAM = "/v1/team";
    public static final String URL_LIST_TEAMS = "/v1/teams";
    public static final String URL_SEARCH_TEAMS = "/v1/teams/search";

    @PostMapping(URL_CREATE_TEAM)
    public ResponseEntity<?> createTeam(@RequestBody CreateReq createReq) {
        Team team = createReq.convertToTeam();
        long id = team.save();
        return ResponseEntity.ok().header("id", id + "").build();
    }

    @GetMapping(URL_LIST_TEAMS)
    public ResponseEntity<Pagination<Team>> list(@RequestParam(name = "page", defaultValue = "1") int pageNum, @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return ResponseEntity.ok().body(Team.list(pageNum, limit));
    }

    @GetMapping(URL_SEARCH_TEAMS)
    public ResponseEntity<Pagination<Team>> search(@RequestParam(name = "q") String q, @RequestParam(name = "page", defaultValue = "1") int pageNum, @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return ResponseEntity.ok().body(Team.search(q, pageNum, limit));
    }

}

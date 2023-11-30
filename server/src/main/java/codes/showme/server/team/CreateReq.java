package codes.showme.server.team;

import codes.showme.domain.team.Team;
import codes.showme.server.auth.Auth;
import codes.showme.server.auth.Subject;
import codes.showme.techlib.ioc.InstanceFactory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateReq {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team convertToTeam() {
        Team result = new Team();
        Auth auth = InstanceFactory.getInstance(Auth.class);
        Subject subject = auth.getSubject();
        result.setCreatorId(subject.getId());
        result.setName(name);
        result.setCreateTime(new Date());
        return result;
    }
}

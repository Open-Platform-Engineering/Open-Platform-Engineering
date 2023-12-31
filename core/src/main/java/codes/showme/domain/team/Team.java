package codes.showme.domain.team;

import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.pagination.Pagination;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = Team.TABLE_NAME)
public class Team {
    public static final String TABLE_NAME = "cp_teams";
    public static final String COLUMN_NAME = "name";

    @Id
    @GeneratedValue
    private long id;

    @Version
    private int version;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = COLUMN_NAME, length = 125, unique = true)
    private String name;

    @Column(name = "creator_id")
    private long creatorId;

    public static Pagination<Team> list(int pageNum, int limit) {
        TeamRepository teamRepository = InstanceFactory.getInstance(TeamRepository.class);
        return teamRepository.listOrderByCreateTime(pageNum, limit);
    }

    public static Pagination<Team> search(String teamName, int pageNum, int limit) {
        TeamRepository teamRepository = InstanceFactory.getInstance(TeamRepository.class);
        return teamRepository.search(teamName, pageNum, limit);
    }

    public long save() {
        TeamRepository teamRepository = InstanceFactory.getInstance(TeamRepository.class);
        return teamRepository.save(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

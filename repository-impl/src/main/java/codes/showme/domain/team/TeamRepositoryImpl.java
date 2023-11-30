package codes.showme.domain.team;

import codes.showme.domain.repository.EbeanConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.pagination.Pagination;
import io.ebean.Database;
import io.ebean.PagedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TeamRepositoryImpl implements TeamRepository {
    private static final Logger logger = LoggerFactory.getLogger(TeamRepositoryImpl.class);

    @Override
    public long save(Team team) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_WRITE_BEONLY);
        database.save(team);
        logger.info("team saved:{},id:{}", team.getName(), team.getId());
        return team.getId();
    }

    @Override
    public Pagination<Team> listOrderByCreateTime(int pageNum, int limit) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_READ_BEONLY);
        int limitInner = Math.min(Pagination.DEFAULT_PAGESIZE, limit);
        int firstRow = (Math.max(pageNum, 1) - 1) * limitInner;
        PagedList<Team> pagedList = database.find(Team.class).setFirstRow(firstRow).setMaxRows(limitInner).findPagedList();
        return new Pagination<>(pageNum, pagedList.getPageSize(), pagedList.getTotalCount(), pagedList.getList());
    }
}

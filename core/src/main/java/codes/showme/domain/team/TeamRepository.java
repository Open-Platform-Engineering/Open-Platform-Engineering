package codes.showme.domain.team;

import codes.showme.techlib.pagination.Pagination;

public interface TeamRepository {
    long save(Team team);

    Pagination<Team> listOrderByCreateTime(int pageNum, int limit);

    Pagination<Team> search(String teamName, int pageNum, int limit);
}

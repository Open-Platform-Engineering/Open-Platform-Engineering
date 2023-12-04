package codes.showme.domain.account;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountRepository {
    long save(Account account);

    void emailValidated(String email);

    Optional<Account> findByEmailAndPassword(String email, String password);

    Optional<Account> findByEmail(String email);

    List<Account> listInTeams(Long[] id);

    List<Account> findAccountByNameWithinTeams(String accountName, Set<Long> teams, int searchUsersLimit);

}

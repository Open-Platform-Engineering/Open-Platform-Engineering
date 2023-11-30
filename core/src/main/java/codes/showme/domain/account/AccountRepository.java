package codes.showme.domain.account;

import java.util.Optional;

public interface AccountRepository {
    long save(Account account);

    void emailValidated(String email);

    Optional<Account> findByEmailAndPassword(String email, String password);

    Optional<Account> findByEmail(String email);
}

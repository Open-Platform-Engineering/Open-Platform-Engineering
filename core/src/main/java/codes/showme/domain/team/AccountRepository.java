package codes.showme.domain.team;

public interface AccountRepository {
    long save(Account account);
    void emailValidated(String email);
}

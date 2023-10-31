package codes.showme.domain.team;

public interface AccountSignUpEvent {
    void fired(Account account);

    boolean validateEmail(String email, String code);
}

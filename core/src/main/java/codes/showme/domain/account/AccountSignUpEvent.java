package codes.showme.domain.account;

public interface AccountSignUpEvent {
    void fired(Account account);

    boolean validateEmail(String email, String code);
}

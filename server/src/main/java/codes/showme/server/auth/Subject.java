package codes.showme.server.auth;

import codes.showme.domain.account.Account;

public class Subject {
    private Account account;

    public long getId() {
        return account.getId();
    }

    public Subject(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

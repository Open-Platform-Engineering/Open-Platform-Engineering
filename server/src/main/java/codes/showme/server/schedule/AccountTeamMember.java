package codes.showme.server.schedule;

import codes.showme.domain.account.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountTeamMember {

    private long id;
    private String name;

    public AccountTeamMember(Account account) {
        this.id = account.getId();
        this.name = account.getDisplayName();
    }

    public static List<AccountTeamMember> convert(List<Account> members) {
        if (members == null) {
            return new ArrayList<>();
        }
        return members.stream().map(member -> {
            return new AccountTeamMember(member);
        }).collect(Collectors.toList());

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

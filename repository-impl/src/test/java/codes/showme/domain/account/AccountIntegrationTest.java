package codes.showme.domain.account;

import codes.showme.domain.AbstractIntegrationTest;
import codes.showme.domain.team.Team;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class AccountIntegrationTest extends AbstractIntegrationTest {

    @Before
    public void init() throws Exception {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        Mockito.when(instanceProvider.getInstance(AccountRepository.class)).thenReturn(accountRepository);
    }

    @Test
    public void save() {
        Account account = new Account();
        String email = "123456@gmail.com";
        account.setEmail(email);
        account.setPassword("abc");
        account.setTeams(Sets.newHashSet(1l, 2l));
        account.save();
        Optional<Account> optionalAccount = Account.findByEmail(email);
        assertTrue(optionalAccount.isPresent());

        Account account1 = optionalAccount.get();
        assertEquals(2, account1.getTeams().size());

        List<Account> list = Account.listInTeams(2l);
        assertEquals(account.getId(), list.get(0).getId());
        assertEquals(1, list.size());

        List<Account> list2 = Account.listInTeams(3l);
        assertEquals(0, list2.size());
        account.joinTeam(3l);
        list2 = Account.listInTeams(3l);
        assertEquals(account.getId(), list2.get(0).getId());
        assertTrue(list2.get(0).getTeams().contains(3l));
        assertEquals(1, list2.size());


    }
}

package codes.showme.domain.account;

import codes.showme.domain.AbstractIntegrationTest;
import codes.showme.techlib.ioc.InstanceFactory;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        account.setDisplayName("abc");
        account.setPassword("abc");
        account.setTeams(Sets.newHashSet(1l, 2l));
        account.save();
        assertEquals(1l, account.getId().longValue());
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

        Account account2 = new Account();
        String email2 = "qwert@gmail.com";
        account2.setEmail(email2);
        account2.setPassword("abc");
        account2.setDisplayName("uiui");
        account2.setTeams(Sets.newHashSet(3l, 2l));
        account2.save();
        assertEquals(2l, account2.getId().longValue());

        List<Account> teamMember = account2.findTeamMember("abc", 20);
        assertEquals(1, teamMember.size());
        assertEquals(account.getId(), teamMember.get(0).getId());

        AccountRepository accountRepository = InstanceFactory.getInstance(AccountRepository.class);
        List<Account> searchUiAccount = accountRepository.findAccountByNameWithinTeams("ui", Sets.newHashSet(2l), 10);
        assertEquals(1, searchUiAccount.size());
        assertEquals(account2.getId(), searchUiAccount.get(0).getId());
        assertEquals(0, accountRepository.findAccountByNameWithinTeams("xxxxx", Sets.newHashSet(2l), 10).size());
        assertEquals(0, accountRepository.findAccountByNameWithinTeams("ui", Sets.newHashSet(7l), 10).size());
        assertEquals(1, accountRepository.findAccountByNameWithinTeams("", Sets.newHashSet(2l), 1).size());
        assertEquals(2, accountRepository.findAccountByNameWithinTeams("", Sets.newHashSet(2l), 2).size());

    }
}

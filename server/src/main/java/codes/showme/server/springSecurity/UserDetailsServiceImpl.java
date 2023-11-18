package codes.showme.server.springSecurity;

import codes.showme.domain.team.Account;
import codes.showme.domain.team.AccountRepository;
import codes.showme.server.account.exceptions.AccountNotFoundException;
import codes.showme.techlib.ioc.InstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = Account.findByEmail(username);
        if (account.isEmpty()) {
            logger.warn("account not found:{}", username);
            throw new AccountNotFoundException();
        }
        return new User(username, account.get().getPassword(), new ArrayList<>());
    }
}

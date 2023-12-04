package codes.showme.domain.account;

import codes.showme.domain.repository.EbeanConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;
import io.ebean.ExpressionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class AccountRepositoryImpl implements AccountRepository {
    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);

    @Override
    public long save(Account account) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_WRITE_BEONLY);
        database.save(account);
        return account.getId();
    }

    @Override
    public void emailValidated(String email) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_WRITE_BEONLY);
        int result = database.sqlUpdate(String.format("update %s set %s=:validated where email =:email", Account.TABLE_NAME, Account.COLUMN_EMAIL_VALIDATED))
                .setParameter("validated", true)
                .setParameter("email", email).execute();
        logger.info("email validated,email:{},result:{}", email, result);

    }

    @Override
    public Optional<Account> findByEmailAndPassword(String email, String password) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_READ_BEONLY);
        return database.find(Account.class)
                .where()
                .eq(Account.COLUMN_EMAIL, email)
                .eq(Account.COLUMN_PASSWORD, password).findOneOrEmpty();
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_READ_BEONLY);
        return database.find(Account.class)
                .where()
                .eq(Account.COLUMN_EMAIL, email).findOneOrEmpty();
    }

    @Override
    public List<Account> listInTeams(Long[] id) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_READ_BEONLY);
        return database.find(Account.class)
                .where().arrayContains(Account.COLUMN_TEAMS, (Object[]) id).findList();
    }

    @Override
    public List<Account> findAccountByNameWithinTeams(String accountName, Set<Long> teams, int limit) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_READ_BEONLY);
        ExpressionList<Account> expressionList = database.find(Account.class).where();
        expressionList.setMaxRows(Math.max(1, limit));
        return expressionList
                .like(Account.COLUMN_DISPLAY_NAME, "%" + accountName + "%")
                .arrayContains(Account.COLUMN_TEAMS, teams.toArray(new Long[teams.size()])).findList();
    }
}

package codes.showme.domain.team;

import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryImpl implements AccountRepository {
    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);
    public static final String DB_BEANNAME_WRITE_ONLY = "write_only";

    @Override
    public long save(Account account) {
        Database database = InstanceFactory.getInstance(Database.class, DB_BEANNAME_WRITE_ONLY);
        database.save(account);
        return account.getId();
    }

    @Override
    public void emailValidated(String email) {
        Database database = InstanceFactory.getInstance(Database.class, DB_BEANNAME_WRITE_ONLY);
        int result = database.sqlUpdate(String.format("update %s set %s=:validated where email =:email", Account.TABLE_NAME, Account.COLUMN_EMAIL_VALIDATED))
                .setParameter("validated", true)
                .setParameter("email", email).execute();
        logger.info("email validated,email:{},result:{}", email, result);
    }
}

package codes.showme.domain.account;

import codes.showme.techlib.ioc.InstanceFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import io.ebean.annotation.DbJson;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;


@Entity
@Table(name = Account.TABLE_NAME)
public class Account {

    public static final String COLUMN_EMAIL_VALIDATED = "email_validated";
    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    public static final int COLUMN_EMAIL_LENGTH = 128;
    public static final String TABLE_NAME = "cp_account";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PASSWORD_SALT = "password_salt";
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "display_name", length = 128)
    private String displayName;

    @Column(name = COLUMN_EMAIL, length = COLUMN_EMAIL_LENGTH)
    private String email;

    @Column(name = COLUMN_PASSWORD, length = 256)
    private String password;

    @Column(name = COLUMN_PASSWORD_SALT, columnDefinition = "bytea")
    private byte[] passwordSalt;

    @Column(name = "password_hash_iterations", length = 2)
    private int passwordHashIterations;

    @Column(name = COLUMN_EMAIL_VALIDATED)
    private boolean emailValidated = false;

    @DbJson
    @Column(name = "profile")
    @JsonProperty("profile")
    private AccountProfile profile;

    public static void signUpSuccess(String email) {
        AccountRepository accountRepository = InstanceFactory.getInstance(AccountRepository.class);
        accountRepository.emailValidated(email);
        logger.info("account's email was validated email:{}", email);
    }

    public static Optional<Account> findByEmail(String email) {
        AccountRepository accountRepository = InstanceFactory.getInstance(AccountRepository.class);
        return accountRepository.findByEmail(email);
    }

    public static Optional<Account> findByEmailAndPassword(String email, String password) {
        AccountRepository accountRepository = InstanceFactory.getInstance(AccountRepository.class);
        return accountRepository.findByEmailAndPassword(email, password);
    }

    public long save() {
        AccountRepository accountRepository = InstanceFactory.getInstance(AccountRepository.class);
        return accountRepository.save(this);
    }

    public void signUpEventFired() {
        if (Objects.isNull(getId()) || Strings.isNullOrEmpty(getEmail())) {
            logger.warn("account's Id or Email is null, can't fire sign up event");
            return;
        }
        AccountSignUpEvent accountSignUpEvent = InstanceFactory.getInstance(AccountSignUpEvent.class);
        accountSignUpEvent.fired(this);
    }

    public void signUp() {
        save();
        signUpEventFired();
    }

    public int getPasswordHashIterations() {
        return passwordHashIterations;
    }

    public void setPasswordHashIterations(int passwordHashIterations) {
        this.passwordHashIterations = passwordHashIterations;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    public AccountProfile getProfile() {
        return profile;
    }

    public void setProfile(AccountProfile profile) {
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

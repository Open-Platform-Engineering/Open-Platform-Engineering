package codes.showme.domain.team;

import codes.showme.techlib.ioc.InstanceFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import io.ebean.annotation.DbJson;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = Account.TABLE_NAME)
public class Account {

    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    public static final int COLUMN_EMAIL_LENGTH = 128;
    public static final String TABLE_NAME = "cp_account";
    public static final String COLUMN_EMAIL_VALIDATED = "email_validated";
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "name", length = 128)
    private String name;

    @Column(name = "email", length = COLUMN_EMAIL_LENGTH)
    private String email;

    @Column(name = "password", length = 32)
    private String password;

    @Column(name = COLUMN_EMAIL_VALIDATED)
    private boolean emailValidated;

    @DbJson
    @Column(name = "profile")
    @JsonProperty("profile")
    private AccountProfile profile;

    public static void signUpSuccess(String email) {
        AccountRepository accountRepository = InstanceFactory.getInstance(AccountRepository.class);
        accountRepository.emailValidated(email);
        logger.info("account's email was validated email:{}", email);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

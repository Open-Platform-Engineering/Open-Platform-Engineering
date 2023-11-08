package codes.showme.server.account;

import codes.showme.domain.team.Account;
import codes.showme.server.auth.ShiroAuthConfiguration;
import codes.showme.techlib.hash.HashService;
import codes.showme.techlib.ioc.InstanceFactory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@JsonIgnoreProperties
public class SignUpReq {

    @JsonProperty("email")
    @Email
    @NotBlank
    private String email;

    @JsonProperty("password")
    @NotBlank
    private String password;

    public Account convertToEntity() {
        Account result = new Account();
        result.setCreateTime(new Date());
        result.setEmail(email);
        HashService hashService = InstanceFactory.getInstance(HashService.class);
        ShiroAuthConfiguration shiroAuthConfiguration = InstanceFactory.getInstance(ShiroAuthConfiguration.class);
        result.setPasswordHashIterations(shiroAuthConfiguration.getPasswordHashIterations());
        HashService.PasswordSaltPair passwordSaltPair = hashService.hash(getPassword(),
                shiroAuthConfiguration.getAlgorithmName(),
                shiroAuthConfiguration.getRandomSaltNum(),
                result.getPasswordHashIterations());
        result.setPasswordSalt(passwordSaltPair.getSalt());
        result.setPassword(passwordSaltPair.getPassword());
        return result;
    }

    @Override
    public String toString() {
        return "SignUpReq{" +
                "email='" + email +
                '}';
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

package codes.showme.server.account;

import codes.showme.domain.team.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@JsonIgnoreProperties
public class SignUpReq {

    @JsonProperty("name")
    @NotBlank
    private String name;

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
        result.setPassword(password);
        return result;
    }

    @Override
    public String toString() {
        return "SignUpReq{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
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

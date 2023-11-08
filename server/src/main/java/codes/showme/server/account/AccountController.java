package codes.showme.server.account;

import codes.showme.domain.team.Account;
import codes.showme.domain.team.AccountSignUpEvent;
import codes.showme.techlib.ioc.InstanceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class AccountController {
    public static final String API_URI_SIGN_UP = "/v1/sign-up";
    public static final String API_URI_SIGN_IN = "/v1/sign-in";
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    public static final String API_URI_SIGNUP_EMAIL_VALIDATE = "/v1/sign/up/email/validate";

    @RequestMapping(value = API_URI_SIGN_UP, method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpReq signUpReq) {
        Account account = signUpReq.convertToEntity();
        account.signUp();
        logger.info("account saved:{},email:{}", account.getId(), account.getEmail());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = API_URI_SIGN_IN, method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInReq signInReq) {
        Subject subject = SecurityUtils.getSubject();
        String username = signInReq.getEmail();
        String password = signInReq.getPassword();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            logger.info("password is incorrect:" + signInReq.getEmail());
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (UnknownAccountException e) {
            logger.info("unknown account:" + signInReq.getEmail());
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException e) {
            logger.info("sign error:{}", signInReq.getEmail(), e);
            throw e;
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = API_URI_SIGNUP_EMAIL_VALIDATE, method = RequestMethod.GET)
    public ResponseEntity<?> signUpValidateEmail(@RequestParam("code") String code,
                                                 @Email @RequestParam("email") String email) {
        AccountSignUpEvent accountSignUpEvent = InstanceFactory.getInstance(AccountSignUpEvent.class);
        boolean pass = accountSignUpEvent.validateEmail(email, code);
        HttpStatus status = pass ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        logger.info("account signUpValidateEmail :{},email:{}, status:{}", code, email, status);
        return new ResponseEntity<>(null, status);
    }

}

package codes.showme.server.account;

import codes.showme.domain.team.Account;
import codes.showme.domain.team.AccountSignUpEvent;
import codes.showme.server.account.authentication.Token;
import codes.showme.techlib.ioc.InstanceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class AccountController {
    public static final String API_URI_SIGN_UP = "/v1/sign-up";
    public static final String API_URI_SIGN_OUT = "/v1/sign-out";
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
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInReq signInReq, HttpServletRequest request,
                                    HttpServletResponse response) {
        String username = signInReq.getEmail();
        String password = signInReq.getPassword();

        AuthenticationManager authenticationManager = InstanceFactory.getInstance(AuthenticationManager.class);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        ));
        String remoteAddr = request.getRemoteAddr();

        Token token = new Token(username, remoteAddr);
        String value = token.saveWithExpiredSeconds(60 * 60 * 24);

        return ResponseEntity.ok().header("token", value).build();

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

    @RequestMapping(value = API_URI_SIGN_OUT, method = RequestMethod.POST)
    public ResponseEntity<?> signOut() {
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.isAuthenticated()) {
//            subject.logout();
//            return new ResponseEntity<>(null, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

}

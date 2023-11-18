package codes.showme.server;

import codes.showme.server.account.exceptions.AccountNotFoundException;
import codes.showme.server.account.exceptions.UnverifiedEmailAccountException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UnverifiedEmailAccountException.class, AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> notVerifiedEmailException(UnverifiedEmailAccountException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        errors.add(ex.getMessage());

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> authenticationException(AuthenticationException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        errors.add(ex.getMessage());

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}

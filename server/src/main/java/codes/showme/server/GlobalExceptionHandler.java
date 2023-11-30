package codes.showme.server;

import codes.showme.server.account.exceptions.AccountNotFoundException;
import codes.showme.server.account.exceptions.UnverifiedEmailAccountException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
    public ResponseEntity<?> notVerifiedEmailException(Exception ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        errors.add(ex.getMessage());

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }


//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseEntity<?> authenticationException(AuthenticationException ex, HttpServletRequest request) {
//        List<String> errors = new ArrayList<>();
//
//        errors.add(ex.getMessage());
//
//        Map<String, List<String>> result = new HashMap<>();
//
//        result.put("errors", errors);
//
//        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
//    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> nullException(NullPointerException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        errors.add("");

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({jakarta.persistence.PersistenceException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<?> persistenceException(Exception ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        errors.add("");

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package account.presentation;

import account.domain.*;
import account.domain.exceptions.BreachedPasswordException;
import account.domain.exceptions.PasswordLengthLessThan12Exception;
import account.domain.exceptions.SamePasswordException;
import account.domain.exceptions.UserExistException;
import account.infrastructure.db.UserIdentityStorage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserIdentityStorage userIdentityStorage;
    private final PasswordEncoder passwordEncoder;

    private final List<String> breachedPasswords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    public AuthController(UserIdentityStorage userIdentityStorage, PasswordEncoder passwordEncoder) {
        this.userIdentityStorage = userIdentityStorage;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping(path = "/test")
    public String test() {
        return "Hello from Auth Service 2000!";
    }

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse signup(@RequestBody @Valid AuthRequest request) {

        boolean exists = userIdentityStorage.existsUserIdentityByEmail(request.email().toLowerCase());

        if (exists) {
            throw new UserExistException();
        }

        if (request.password().length() < 12) {
            throw new PasswordLengthLessThan12Exception();
        }

        if (breachedPasswords.contains(request.password())) {
            throw new BreachedPasswordException();
        }

        var userIdentity = new UserIdentity();
        userIdentity.setName(request.name());
        userIdentity.setLastname(request.lastname());
        userIdentity.setEmail(request.email().toLowerCase());
        userIdentity.setPassword(passwordEncoder.encode(request.password()));
        userIdentity.setAuthority("ROLE_USER");

        UserIdentity result = userIdentityStorage.save(userIdentity);

        return new AuthResponse(result.getId(), result.getName(), result.getLastname(), result.getEmail());
    }

    @PostMapping(path = "/changepass", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChangepassResponse changepass(@RequestBody ChangepassRequest request, @AuthenticationPrincipal UserDetails details) {
        if (passwordEncoder.matches(request.new_password(), details.getPassword())) {
            throw new SamePasswordException();
        }

        if (request.new_password().length() < 12) {
            throw new PasswordLengthLessThan12Exception();
        }

        if (breachedPasswords.contains(request.new_password())) {
            throw new BreachedPasswordException();
        }

        UserIdentity userIdentity = userIdentityStorage.findUserIdentityByEmail(details.getUsername().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Unknown error while changepass"));

        userIdentity.setPassword(passwordEncoder.encode(request.new_password()));
        userIdentityStorage.save(userIdentity);

        return new ChangepassResponse(
                details.getUsername(),
                "The password has been updated successfully"
        );
    }


    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) throws IOException {
        var body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "User exist!",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleNotValidArgument(HttpServletRequest request) throws IOException {
        var body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(PasswordLengthLessThan12Exception.class)
    public ResponseEntity<ErrorResponse> handleTooShortPassword(HttpServletRequest request) throws IOException {
        var body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Password length must be 12 chars minimum!",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ErrorResponse> handleSamePasswordError(HttpServletRequest request) {
        var body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "The passwords must be different!",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BreachedPasswordException.class)
    public ResponseEntity<ErrorResponse> handleBreachedPasswordError(HttpServletRequest request) {
        var body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "The password is in the hacker's database!",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}

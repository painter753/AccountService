package account.presentation;

import account.domain.AuthRequest;
import account.domain.AuthResponse;
import account.domain.ErrorResponse;
import account.domain.UserIdentity;
import account.domain.exceptions.UserExistException;
import account.infrastructure.db.UserIdentityStorage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserIdentityStorage userIdentityStorage;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserIdentityStorage userIdentityStorage, PasswordEncoder passwordEncoder) {
        this.userIdentityStorage = userIdentityStorage;
        this.passwordEncoder = passwordEncoder;
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
    public ResponseEntity<ErrorResponse> handleCommonError(HttpServletRequest request) throws IOException {
        var body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad request!",
                "",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
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

        var userIdentity = new UserIdentity();
        userIdentity.setName(request.name());
        userIdentity.setLastname(request.lastname());
        userIdentity.setEmail(request.email().toLowerCase());
        userIdentity.setPassword(passwordEncoder.encode(request.password()));
        userIdentity.setAuthority("ROLE_USER");

        UserIdentity result = userIdentityStorage.save(userIdentity);

        return new AuthResponse(result.getId(), result.getName(), result.getLastname(), result.getEmail());
    }


}

package account.presentation;

import account.domain.AuthRequest;
import account.domain.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse signup(@RequestBody @Valid AuthRequest request) {
        return new AuthResponse(request.name(), request.lastname(), request.email());
    }

//    @PostMapping(path = "/changepass", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public

}

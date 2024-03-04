package account.presentation;

import account.application.UserIdentityService;
import account.application.dto.auth.AuthRequest;
import account.application.dto.auth.AuthResponse;
import account.presentation.dto.AuthRequestDto;
import account.presentation.dto.AuthResponseDto;
import account.application.dto.auth.ChangepassRequest;
import account.application.dto.auth.ChangepassResponse;
import account.presentation.dto.ChangepassRequestDto;
import account.presentation.dto.ChangepassResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserIdentityService userIdentityService;

    public AuthController(UserIdentityService userIdentityService) {
        this.userIdentityService = userIdentityService;
    }

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponseDto signup(@RequestBody @Valid AuthRequestDto request) {
        return mapToAuthDto(userIdentityService.signUpUser(mapFromAuthDto(request)));
    }

    @PostMapping(path = "/changepass", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChangepassResponseDto changepass(@RequestBody ChangepassRequestDto request) {
        return mapToChangepassDto(userIdentityService.changeUserPassword(mapFromChangepassDto(request)));
    }

    private AuthRequest mapFromAuthDto(AuthRequestDto request) {
        return new AuthRequest(request.name(), request.lastname(), request.email(), request.password());
    }

    private AuthResponseDto mapToAuthDto(AuthResponse response) {
        return new AuthResponseDto(response.id(), response.name(), response.lastname(), response.email());
    }

    private ChangepassRequest mapFromChangepassDto(ChangepassRequestDto request) {
        return new ChangepassRequest(request.newPassword());
    }

    private ChangepassResponseDto mapToChangepassDto(ChangepassResponse response) {
        return new ChangepassResponseDto(response.email(), response.status());
    }

}

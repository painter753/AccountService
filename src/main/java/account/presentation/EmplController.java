package account.presentation;

import account.domain.EmplResponse;
import account.domain.UserIdentity;
import account.infrastructure.db.UserIdentityStorage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empl")
public class EmplController {

    private final UserIdentityStorage userIdentityStorage;

    public EmplController(UserIdentityStorage userIdentityStorage) {
        this.userIdentityStorage = userIdentityStorage;
    }

    @GetMapping(value = "/payment")
    public EmplResponse payment(@AuthenticationPrincipal UserDetails details) {
        UserIdentity userIdentity = userIdentityStorage.findUserIdentityByEmail(details.getUsername().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Unknown error"));

        return new EmplResponse(userIdentity.getId(), userIdentity.getName(), userIdentity.getLastname(), userIdentity.getEmail());
    }


}

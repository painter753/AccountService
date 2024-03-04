package account.application;

import account.application.dto.auth.AuthRequest;
import account.application.dto.auth.AuthResponse;
import account.application.dto.auth.ChangepassRequest;
import account.application.dto.auth.ChangepassResponse;
import account.domain.exceptions.BreachedPasswordException;
import account.domain.exceptions.PasswordLengthLessThan12Exception;
import account.domain.exceptions.SamePasswordException;
import account.domain.exceptions.UserExistsException;
import account.infrastructure.db.UserIdentityDto;
import account.infrastructure.db.UserIdentityStorage;
import account.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserIdentityServiceImpl implements UserIdentityService {

    private final UserIdentityStorage userIdentityStorage;
    private final CurrentUserProvider currentUserProvider;
    private final PasswordEncoder passwordEncoder;

    private final List<String> breachedPasswords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    public UserIdentityServiceImpl(UserIdentityStorage userIdentityStorage, CurrentUserProvider currentUserProvider, PasswordEncoder passwordEncoder) {
        this.userIdentityStorage = userIdentityStorage;
        this.currentUserProvider = currentUserProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse signUpUser(AuthRequest request) {
        boolean exists = userIdentityStorage.existsUserIdentityByEmailIgnoreCase(request.email().toLowerCase());

        if (exists) {
            throw new UserExistsException();
        }

        if (request.password().length() < 12) {
            throw new PasswordLengthLessThan12Exception();
        }

        if (breachedPasswords.contains(request.password())) {
            throw new BreachedPasswordException();
        }

        var user = new User(
                null,
                request.name(),
                request.lastname(),
                request.email(),
                request.password()
        );

        UserIdentityDto result = userIdentityStorage.save(mapToDto(user));

        return new AuthResponse(result.getId(), result.getName(), result.getLastname(), result.getEmail());
    }

    private UserIdentityDto mapToDto(User user) {
        var userIdentity = new UserIdentityDto();
        userIdentity.setName(user.name());
        userIdentity.setLastname(user.lastname());
        userIdentity.setEmail(user.email());
        userIdentity.setPassword(passwordEncoder.encode(user.password()));
        userIdentity.setAuthority("ROLE_USER");

        return userIdentity;
    }

    @Override
    public ChangepassResponse changeUserPassword(ChangepassRequest request) {
        String currentPassword = currentUserProvider.getCurrentUser().getPassword();
        String username = currentUserProvider.getCurrentUser().getUsername();

        if (passwordEncoder.matches(request.newPassword(), currentPassword)) {
            throw new SamePasswordException();
        }

        if (request.newPassword().length() < 12) {
            throw new PasswordLengthLessThan12Exception();
        }

        if (breachedPasswords.contains(request.newPassword())) {
            throw new BreachedPasswordException();
        }

        UserIdentityDto userIdentityDto = userIdentityStorage.findUserIdentityByEmailIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("Unknown error while changepass"));

        userIdentityDto.setPassword(passwordEncoder.encode(request.newPassword()));
        userIdentityStorage.save(userIdentityDto);

        return new ChangepassResponse(
                username,
                "The password has been updated successfully"
        );
    }
}

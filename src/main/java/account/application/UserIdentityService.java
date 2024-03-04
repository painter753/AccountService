package account.application;

import account.application.dto.auth.AuthRequest;
import account.application.dto.auth.AuthResponse;
import account.application.dto.auth.ChangepassRequest;
import account.application.dto.auth.ChangepassResponse;

public interface UserIdentityService {
    AuthResponse signUpUser(AuthRequest request);

    ChangepassResponse changeUserPassword(ChangepassRequest request);
}

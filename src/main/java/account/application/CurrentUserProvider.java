package account.application;

import org.springframework.security.core.userdetails.UserDetails;

public interface CurrentUserProvider {
    UserDetails getCurrentUser();
}

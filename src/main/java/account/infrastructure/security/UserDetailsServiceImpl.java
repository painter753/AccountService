package account.infrastructure.security;

import account.domain.UserDetailsAdapter;
import account.domain.UserIdentity;
import account.infrastructure.db.UserIdentityStorage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserIdentityStorage userIdentityStorage;

    public UserDetailsServiceImpl(UserIdentityStorage userIdentityStorage) {
        this.userIdentityStorage = userIdentityStorage;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserIdentity user = userIdentityStorage.findUserIdentityByEmail(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        return new UserDetailsAdapter(user);
    }
}

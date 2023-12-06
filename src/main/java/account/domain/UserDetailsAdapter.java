package account.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsAdapter implements UserDetails {
    private final UserIdentity userIdentity;

    public UserDetailsAdapter(UserIdentity userIdentity) {
        this.userIdentity = userIdentity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userIdentity.getAuthority()));
    }

    @Override
    public String getPassword() {
        return userIdentity.getPassword();
    }

    @Override
    public String getUsername() {
        return userIdentity.getEmail().toLowerCase();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

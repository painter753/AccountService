package account.domain;

import account.infrastructure.db.UserIdentityDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsAdapter implements UserDetails {
    private final UserIdentityDto userIdentityDto;

    public UserDetailsAdapter(UserIdentityDto userIdentityDto) {
        this.userIdentityDto = userIdentityDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userIdentityDto.getAuthority()));
    }

    @Override
    public String getPassword() {
        return userIdentityDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userIdentityDto.getEmail().toLowerCase();
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

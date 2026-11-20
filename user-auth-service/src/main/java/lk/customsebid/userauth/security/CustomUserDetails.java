package lk.customsebid.userauth.security;

import lk.customsebid.userauth.entity.User;
import lk.customsebid.userauth.entity.UserStatus;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final String authority;
    private final boolean enabled;
    private final boolean accountNonLocked;
    private final long credentialVersion;

    public CustomUserDetails(User user) {
        this.email = user.getEmail();
        this.password = user.getPasswordHash();
        this.authority = "ROLE_" + user.getRole().name();
        this.enabled = user.getStatus() != UserStatus.INACTIVE;
        this.accountNonLocked = user.getStatus() != UserStatus.SUSPENDED;
        this.credentialVersion = user.getCredentialVersion();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public long getCredentialVersion() {
        return credentialVersion;
    }
}
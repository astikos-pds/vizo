package br.app.vizo.config.security;

import br.app.vizo.domain.user.Citizen;
import br.app.vizo.domain.user.Municipality;
import br.app.vizo.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.user instanceof Citizen)
            return List.of(new SimpleGrantedAuthority("ROLE_CITIZEN"));
        else if (this.user instanceof Municipality)
            return List.of(new SimpleGrantedAuthority("ROLE_MUNICIPALITY"));
        return List.of();
    }

    @Override
    public String getUsername() {
        return user.getDocument();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
}

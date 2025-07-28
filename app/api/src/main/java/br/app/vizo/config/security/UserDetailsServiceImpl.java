package br.app.vizo.config.security;

import br.app.vizo.domain.user.User;
import br.app.vizo.repository.OldUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final OldUserRepository oldUserRepository;

    public UserDetailsServiceImpl(OldUserRepository oldUserRepository) {
        this.oldUserRepository = oldUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.oldUserRepository.findByDocument(username).orElseThrow(
                () -> new RuntimeException("User not found.")
        );
        return new UserDetailsImpl(user);
    }
}

package br.app.vizo.application;

import br.app.vizo.core.user.password.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordHasherImpl implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String hash(String raw) {
        return this.passwordEncoder.encode(raw);
    }

    @Override
    public boolean matches(String raw, String hashed) {
        return this.passwordEncoder.matches(raw, hashed);
    }
}

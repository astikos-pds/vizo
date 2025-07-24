package br.app.vizo.application.factory;

import br.app.vizo.core.shared.Email;
import br.app.vizo.core.user.Document;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserFactory;
import br.app.vizo.core.user.password.HashedPassword;
import br.app.vizo.core.user.password.HashedPasswordFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactoryImpl implements UserFactory {

    private final HashedPasswordFactory hashedPasswordFactory;

    public User create(String name, String document, String email, String password) {
        HashedPassword hashedPassword = this.hashedPasswordFactory.create(password);

        return new User(name, new Document(document), new Email(email), hashedPassword);
    }
}

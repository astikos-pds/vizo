package br.app.vizo.application.factory;

import br.app.vizo.application.Factory;
import br.app.vizo.core.shared.*;
import br.app.vizo.core.user.Document;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserFactory;
import br.app.vizo.core.user.password.HashedPassword;
import br.app.vizo.core.user.password.HashedPasswordFactory;

@Factory
public class UserFactoryImpl implements UserFactory {

    private final HashedPasswordFactory hashedPasswordFactory;

    public UserFactoryImpl(HashedPasswordFactory hashedPasswordFactory) {
        this.hashedPasswordFactory = hashedPasswordFactory;
    }

    @Override
    public User create(String name, String document, String email, String password) {
        HashedPassword hashedPassword = this.hashedPasswordFactory.create(password);

        return new User(new Name(name), new Document(document), new Email(email), hashedPassword);
    }
}

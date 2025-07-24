package br.app.vizo.core.user;

import br.app.vizo.core.shared.*;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String name;
    private final Document document;
    private Email email;
    private HashedPassword password;
    private Media avatar;
    private final Credibility credibility;
    private final MutationTimestamps timestamps;

    private User(
            UUID id,
            String name,
            Document document,
            Email email,
            HashedPassword password,
            Media avatar,
            Credibility credibility,
            MutationTimestamps timestamps
    ) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.credibility = credibility;
        this.timestamps = timestamps;
    }

    public User(String name, Document document, Email email, HashedPassword password) {
        this(UUID.randomUUID(), name, document, email, password, new Media(), new Credibility(), MutationTimestamps.create());
    }

    public static User register(String name, String document, String email, String password, PasswordHasher passwordHasher) {
        HashedPassword hashedPassword = new Password(password).hash(passwordHasher);

        return new User(name, new Document(document), new Email(email), hashedPassword);
    }

    public void changePassword(String newPassword, PasswordHasher passwordHasher) {
        this.password = new Password(newPassword).hash(passwordHasher);
        this.timestamps.update();
    }

    public void changeEmail(String newEmail) {
        this.email = new Email(newEmail);
        this.timestamps.update();
    }

    public void updateAvatar(Media avatar) {
        this.avatar = avatar;
        this.timestamps.update();
    }

    public void increaseCredibility(Double delta) {
        this.credibility.increase(delta);
    }

    public void decreaseCredibility(Double delta) {
        this.credibility.decrease(delta);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getDocument() {
        return this.document.getValue();
    }

    public String getEmail() {
        return this.email.getValue();
    }

    public String getAvatarUrl() {
        return this.avatar.getUrl();
    }

}

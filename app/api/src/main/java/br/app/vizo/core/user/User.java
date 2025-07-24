package br.app.vizo.core.user;

import br.app.vizo.core.shared.*;
import br.app.vizo.core.user.password.HashedPassword;
import lombok.Getter;

import java.util.UUID;

public class User {

    @Getter private final UUID id;
    @Getter private final String name;
    private final Document document;
    private Email email;
    @Getter private HashedPassword password;
    private Media avatar;
    private Credibility credibility;
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

    public void changePassword(HashedPassword password) {
        this.password = password;
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
        this.credibility = this.credibility.increase(delta);
    }

    public void decreaseCredibility(Double delta) {
        this.credibility = this.credibility.decrease(delta);
    }

    public String getDocument() {
        return this.document.getValue();
    }

    public String getEmail() {
        return this.email.getValue();
    }

    public String getAvatarUrl() {
        return this.avatar.url();
    }

}

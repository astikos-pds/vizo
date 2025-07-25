package br.app.vizo.core.user;

import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.shared.*;
import br.app.vizo.core.user.password.HashedPassword;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Getter private final UUID id;
    @Getter private final String name;
    private final Document document;
    private Email email;
    @Getter private HashedPassword password;
    private Image avatar;
    private Credibility credibility;
    private final MutationTimestamps timestamps;

    public User(String name, Document document, Email email, HashedPassword password) {
        this(UUID.randomUUID(), name, document, email, password, null, new Credibility(), MutationTimestamps.create());
    }

    public Report report(Problem problem, String description, Double latitude, Double longitude, Set<String> imageUrls) {
        return new Report(this, problem, description, latitude, longitude, imageUrls);
    }

    public void changePassword(HashedPassword password) {
        this.password = password;
        this.timestamps.update();
    }

    public void changeEmail(String newEmail) {
        this.email = new Email(newEmail);
        this.timestamps.update();
    }

    public void updateAvatar(Image avatar) {
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
        return this.document.value();
    }

    public String getEmail() {
        return this.email.value();
    }

    public String getAvatarUrl() {
        return this.avatar.url();
    }

}

package br.app.vizo.core.user;

import br.app.vizo.core.affiliation.AffiliationIntent;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.shared.*;
import br.app.vizo.core.user.password.HashedPassword;
import br.app.vizo.core.user.password.PasswordHasher;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class User {

    private final UserId id;
    private final Name name;
    private final Document document;
    private Email email;
    private HashedPassword password;
    private Image avatar;
    private Credibility credibility;
    private final MutationTimestamps timestamps;

    public User(UUID id, String name, String document, String email, String password, String avatarUrl, Double credibilityPoints, Instant createdAt, Instant updatedAt) {
        this.id = new UserId(id);
        this.name = new Name(name);
        this.document = new Document(document);
        this.email = new Email(email);
        this.password = new HashedPassword(password);
        this.avatar = avatarUrl == null ? null : new Image(avatarUrl);
        this.credibility = new Credibility(credibilityPoints);
        this.timestamps = new MutationTimestamps(createdAt, updatedAt);
    }

    public User(Name name, Document document, Email email, HashedPassword password) {
        this.id = new UserId();
        this.name = name;
        this.document = document;
        this.email = email;
        this.password = password;
        this.avatar = null;
        this.credibility = new Credibility();
        this.timestamps = MutationTimestamps.create();
    }

    public Report report(Problem problem, String description, Double latitude, Double longitude, Set<String> imageUrls, Double credibility) {
        Report report = new Report(this, problem, description, latitude, longitude, imageUrls, credibility);

        problem.increaseCredibility(report.getCredibility());

        return report;
    }

    public AffiliationIntent requestAffiliationTo(Municipality municipality) {
        return new AffiliationIntent(this, municipality);
    }

    public void updatePassword(HashedPassword password) {
        this.password = password;
        this.timestamps.update();
    }

    public void updateEmail(String newEmail) {
        this.email = new Email(newEmail);
        this.timestamps.update();
    }

    public void updateAvatar(Image avatar) {
        this.avatar = avatar;
        this.timestamps.update();
    }

    public void increaseCredibility(Double delta) {
        this.credibility = this.credibility.accumulate(delta);
    }

    public boolean passwordMatchesWith(String password, PasswordHasher passwordHasher) {
        return this.password.matches(password, passwordHasher);
    }

    public UUID getId() {
        return id.value();
    }

    public String getName() {
        return this.name.value();
    }

    public String getDocument() {
        return this.document.value();
    }

    public String getEmail() {
        return this.email.value();
    }

    public String getPassword() {
        return this.password.value();
    }

    public String getAvatarUrl() {
        return this.avatar == null ? null : avatar.url();
    }

    public Double getCredibilityPoints() {
        return credibility.points();
    }

    public Instant getCreatedAt() {
        return timestamps.getCreatedAt();
    }

    public Instant getUpdatedAt() {
        return timestamps.getUpdatedAt();
    }

    public boolean isSameAs(User other) {
        return id.equals(other.getId());
    }
}

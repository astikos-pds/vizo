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

    public User(UserId id, Name name, Document document, Email email, HashedPassword password, Image avatar, Credibility credibility, MutationTimestamps timestamps) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.credibility = credibility;
        this.timestamps = timestamps;
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
        return new Report(this, problem, description, latitude, longitude, imageUrls, credibility);
    }

    public AffiliationIntent requestAffiliationTo(Municipality municipality) {
        return new AffiliationIntent(this, municipality);
    }

    public void changePassword(HashedPassword password) {
        this.password = password;
        this.timestamps.update();
    }

    public void changeEmail(String newEmail) {
        this.email = new Email(newEmail);
        this.timestamps.update();
    }

    public void changeAvatar(Image avatar) {
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

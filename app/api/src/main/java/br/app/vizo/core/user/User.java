package br.app.vizo.core.user;

import br.app.vizo.core.affiliation.AffiliationIntent;
import br.app.vizo.core.municipality.Municipality;
import br.app.vizo.core.problem.Problem;
import br.app.vizo.core.report.Report;
import br.app.vizo.core.shared.*;
import br.app.vizo.core.user.password.HashedPassword;
import br.app.vizo.core.user.password.PasswordHasher;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id private UUID id;
    @Embedded private Name name;
    @Embedded private Document document;
    @Embedded private Email email;
    @Embedded private HashedPassword password;
    @AttributeOverride(name = "url", column = @Column(name = "avatar_url"))
    @Embedded private Image avatar;
    @Embedded private Credibility credibility;
    @Embedded private MutationTimestamps timestamps;

    public User() {
    }

    public User(Name name, Document document, Email email, HashedPassword password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.document = document;
        this.email = email;
        this.password = password;
        this.avatar = null;
        this.credibility = new Credibility();
        this.timestamps = MutationTimestamps.create();
    }

    public Report report(Problem problem, String description, Double latitude, Double longitude, Set<String> imageUrls) {
        return new Report(this, problem, description, latitude, longitude, imageUrls);
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
        this.credibility = this.credibility.increase(delta);
    }

    public void decreaseCredibility(Double delta) {
        this.credibility = this.credibility.decrease(delta);
    }

    public boolean passwordMatchesWith(String password, PasswordHasher passwordHasher) {
        return this.password.matches(password, passwordHasher);
    }

    public UUID getId() {
        return id;
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

package br.app.vizo.core.municipality;

import br.app.vizo.core.shared.Email;
import br.app.vizo.core.shared.Image;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.core.shared.Name;

import java.time.Instant;
import java.util.UUID;

public class Municipality {

    private final UUID id;
    private Name name;
    private EmailDomain emailDomain;
    private Image icon;
    private final MutationTimestamps timestamps;

    public Municipality(UUID id, Name name, EmailDomain emailDomain, Image icon, MutationTimestamps timestamps) {
        this.id = id;
        this.name = name;
        this.emailDomain = emailDomain;
        this.icon = icon;
        this.timestamps = timestamps;
    }

    public void updateName(String name) {
        this.name = new Name(name);
        this.timestamps.update();
    }

    public void updateEmailDomain(String emailDomain) {
        this.emailDomain = new EmailDomain(emailDomain);
        this.timestamps.update();
    }

    public void updateIcon(String url) {
        this.icon = new Image(url);
        this.timestamps.update();
    }

    public boolean acceptsEmail(String email) {
        return this.emailDomain.matches(new Email(email));
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public String getEmailDomain() {
        return emailDomain.value();
    }

    public String getIconUrl() {
        return icon == null ? null : icon.url();
    }

    public Instant getCreatedAt() {
        return timestamps.getCreatedAt();
    }

    public Instant getUpdatedAt() {
        return timestamps.getUpdatedAt();
    }

    public boolean isSameAs(Municipality other) {
        return this.id.equals(other.getId());
    }
}

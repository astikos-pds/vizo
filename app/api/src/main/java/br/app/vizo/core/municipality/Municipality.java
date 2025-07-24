package br.app.vizo.core.municipality;

import br.app.vizo.core.shared.Email;
import br.app.vizo.core.shared.Image;
import br.app.vizo.core.shared.MutationTimestamps;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Municipality {

    @Getter
    private final UUID id;

    @Getter
    private String name;

    private EmailDomain emailDomain;

    private Image icon;

    private final MutationTimestamps timestamps;

    public Municipality(String name, String emailDomain, String iconUrl) {
        this(
                UUID.randomUUID(),
                name,
                new EmailDomain(emailDomain),
                new Image(iconUrl),
                MutationTimestamps.create()
        );
    }

    public void updateName(String name) {
        this.name = name;
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

    public boolean canAcceptEmail(Email email) {
        return this.emailDomain.matches(email);
    }

    public String getIconUrl() {
        return icon.url();
    }

    public Instant getCreatedAt() {
        return timestamps.getCreatedAt();
    }

}

package br.app.vizo.core.shared;

import java.time.Instant;

public record ExpirationTimestamp(
        Instant value
) {

    public static ExpirationTimestamp fromNowPlusMinutes(int minutes) {
        return new ExpirationTimestamp(Instant.now().plusSeconds(minutes * 60L));
    }

    public static ExpirationTimestamp fromNowPlusHours(int hours) {
        return new ExpirationTimestamp(Instant.now().plusSeconds(hours * 60L * 60L));
    }

    public boolean isExpired() {
        return Instant.now().isAfter(value);
    }

}

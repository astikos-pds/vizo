package br.app.vizo.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtil {

    public static Instant now() {
        return ZonedDateTime
                .now(ZoneId.of("America/Sao_Paulo"))
                .toInstant();
    }
}

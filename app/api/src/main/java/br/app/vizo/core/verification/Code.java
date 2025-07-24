package br.app.vizo.core.verification;

import br.app.vizo.core.verification.exception.InvalidCodeException;

import java.util.Random;

public class Code {

    private static final int LENGTH = 6;

    private final String value;

    public Code(String value) {
        if (value == null || !value.matches("\\d{" + LENGTH + "}")) {
            throw new InvalidCodeException("Code must be a " + LENGTH + "-digit numeric string.");
        }

        this.value = value;
    }

    public static Code generate() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < LENGTH; i++) {
            sb.append(random.nextInt(0, 10));
        }

        return new Code(sb.toString());
    }

    public boolean matches(String rawCode) {
        return this.value.equals(rawCode);
    }
}

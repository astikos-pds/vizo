package br.app.vizo.core.verification;

public interface EmailVerificationRequestFactory {

    EmailVerificationRequest create(String rawEmail);
}

package br.app.vizo.application.factory;

import br.app.vizo.application.Factory;
import br.app.vizo.core.shared.Email;
import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.shared.Code;
import br.app.vizo.core.verification.EmailVerificationRequest;
import br.app.vizo.core.verification.EmailVerificationRequestFactory;
import br.app.vizo.core.verification.VerificationPurpose;

@Factory
public class EmailVerificationRequestFactoryImpl implements EmailVerificationRequestFactory {

    @Override
    public EmailVerificationRequest create(String rawEmail, VerificationPurpose purpose) {
        Email email = new Email(rawEmail);
        Code code = Code.generate();

        ExpirationTimestamp expiresAt = ExpirationTimestamp.fromNowPlusMinutes(15);

        return new EmailVerificationRequest(email, code, false, purpose, expiresAt);
    }
}

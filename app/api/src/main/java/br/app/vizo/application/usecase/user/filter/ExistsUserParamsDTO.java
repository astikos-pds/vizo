package br.app.vizo.application.usecase.user.filter;

import br.app.vizo.core.shared.Email;
import br.app.vizo.core.user.Document;

public record ExistsUserParamsDTO(
        Email email,
        Document document
) {

    public ExistsUserParamsDTO(String email, String document) {
        this(new Email(email), document == null ? null : new Document(document));
    }

    public String getEmail() {
        return email.value();
    }

    public String getDocument() {
        return document.value();
    }
}

package br.app.vizo.application.usecase.user.filter;

import br.app.vizo.core.shared.Email;
import br.app.vizo.core.user.Document;

public record GetUserParamsDTO(
        Email email,
        Document document
) {

    public GetUserParamsDTO(String email, String document) {
        this(new Email(email), new Document(document));
    }

    public String getEmail() {
        return email.value();
    }

    public String getDocument() {
        return document.value();
    }
}

package br.app.vizo.application.exception.auth;

import br.app.vizo.application.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User not found.");
    }
}

package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class NotificationNotFoundException extends NotFoundException {
    public NotificationNotFoundException() {
        super("Notification not found.");
    }
}

package br.app.vizo.core.notification.exception;

public class InvalidNotificationException extends RuntimeException {
    public InvalidNotificationException() {
        super("None of notification's fields can be null.");
    }
}

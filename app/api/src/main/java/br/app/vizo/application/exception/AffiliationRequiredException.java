package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class AffiliationRequiredException extends ForbiddenException {
  public AffiliationRequiredException() {
    super("You need to be affiliated to execute this action.");
  }
}

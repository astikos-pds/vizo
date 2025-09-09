package br.app.vizo.core.affiliation.exception;

import br.app.vizo.core.DomainException;

public class CannotAssignAnUnapprovedAffiliateException extends DomainException {
    public CannotAssignAnUnapprovedAffiliateException() {
        super("You cannot assign an unapproved affiliate to an department.");
    }
}

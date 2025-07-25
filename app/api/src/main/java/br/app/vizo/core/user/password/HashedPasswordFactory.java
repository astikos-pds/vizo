package br.app.vizo.core.user.password;

public interface HashedPasswordFactory {

    HashedPassword create(String password);
}

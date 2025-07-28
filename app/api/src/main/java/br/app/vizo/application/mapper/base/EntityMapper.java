package br.app.vizo.application.mapper.base;

public interface EntityMapper<MODEL, ENTITY> {

    ENTITY toEntity(MODEL model);
}

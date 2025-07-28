package br.app.vizo.application.mapper.base;

public interface ModelMapper<ENTITY, MODEL> {

    MODEL toModel(ENTITY entity);
}

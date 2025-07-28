package br.app.vizo.application.mapper.base;

public interface DtoMapper<MODEL, DTO> {

    DTO toDto(MODEL model);
}

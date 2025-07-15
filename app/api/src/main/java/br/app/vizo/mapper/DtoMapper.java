package br.app.vizo.mapper;

public interface DtoMapper<E, DTO> {

    DTO toDto(E entity);
}

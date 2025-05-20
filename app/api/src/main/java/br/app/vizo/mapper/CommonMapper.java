package br.app.vizo.mapper;

public interface CommonMapper<E, DTO> {

    DTO toDto(E entity);
    E toEntity(DTO dto);
}

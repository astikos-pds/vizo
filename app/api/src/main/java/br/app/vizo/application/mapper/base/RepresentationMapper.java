package br.app.vizo.application.mapper.base;

public interface RepresentationMapper<MODEL, ENTITY, DTO> extends
        ModelMapper<ENTITY, MODEL>,
        EntityMapper<MODEL, ENTITY>,
        DtoMapper<MODEL, DTO>
{
}

package br.app.vizo.mapper;

import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AffiliationRequestMapper {

    @Mapping(source = "official.id", target = "officialId")
    @Mapping(source = "municipality.id", target = "municipalityId")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    AffiliationRequestDTO toDto(AffiliationRequest affiliationRequest);
}

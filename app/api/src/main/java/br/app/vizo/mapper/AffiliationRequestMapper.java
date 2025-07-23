package br.app.vizo.mapper;

import br.app.vizo.dto.AffiliationRequestDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {OfficialMapper.class, MunicipalityMapper.class})
public interface AffiliationRequestMapper extends DtoMapper<AffiliationRequest, AffiliationRequestDTO> {
}

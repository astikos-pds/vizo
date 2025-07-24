package br.app.vizo.mapper;

import br.app.vizo.dto.AffiliationDTO;
import br.app.vizo.domain.affiliation.Affiliation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MunicipalityMapper.class})
public interface AffiliationMapper extends DtoMapper<Affiliation, AffiliationDTO> {
}

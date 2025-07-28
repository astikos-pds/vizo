package br.app.vizo.mapper;

import br.app.vizo.dto.AffiliationDTO;
import br.app.vizo.domain.affiliation.Affiliation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OldUserMapper.class, OldMunicipalityMapper.class})
public interface OldAffiliationMapper extends DtoMapper<Affiliation, AffiliationDTO> {
}

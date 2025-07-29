package br.app.vizo.application.dto.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PaginationDTO(
        int page,
        int size
) {

    public static Pageable resolve(PaginationDTO paginationDTO) {
        return paginationDTO == null
                ? PageRequest.of(0, 20)
                : PageRequest.of(paginationDTO.page(), paginationDTO.size());
    }
}

package br.app.vizo.application.dto.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PaginationDTO(
        Integer page,
        Integer size
) {

    public static Pageable resolve(PaginationDTO paginationDTO) {
        if (paginationDTO == null) {
            return PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "createdAt"));
        }

        int page = paginationDTO.page() == null ? 0 : paginationDTO.page();
        int size = paginationDTO.size() == null ? 20 : paginationDTO.size();

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}

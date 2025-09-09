package br.app.vizo.application.dto.page;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageDTO<T>(
        List<T> content,
        int page,
        int size,
        int totalPages,
        long totalElements
) {

    public static <T> PageDTO<T> of(Page<T> page) {
        return new PageDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    public <R> PageDTO<R> map(Function<T, R> mapper) {
        return new PageDTO<>(
                content.stream().map(mapper).toList(),
                page,
                size,
                totalPages,
                totalElements
        );
    }
}

package br.app.vizo.controller.request;

public record CreateDepartmentRequestDTO(
        String name,
        String iconUrl,
        String colorHex
) {
}

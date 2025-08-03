package br.app.vizo.application.usecase.assignment.request;

import java.util.List;
import java.util.UUID;

public record AssignUsersToDepartmentRequestDTO(List<UUID> ids) {
}

package br.app.vizo.controller.request;

import java.util.List;
import java.util.UUID;

public record BatchUpdateAssignmentRequestDTO(
        List<UUID> ids
) {
}

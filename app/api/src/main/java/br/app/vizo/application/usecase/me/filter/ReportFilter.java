package br.app.vizo.application.usecase.me.filter;

public record ReportFilter(
        Double latitude,
        Double longitude,
        Double radius
) {
}

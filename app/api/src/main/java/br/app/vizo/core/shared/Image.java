package br.app.vizo.core.shared;

public record Image(
        String url
) {

    public Image() {
        this("");
    }
}

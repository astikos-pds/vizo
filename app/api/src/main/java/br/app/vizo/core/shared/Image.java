package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidImage;

public record Image(
        String url
) {

    public Image {
        if (url == null || url.isBlank()) {
            throw new InvalidImage();
        }
    }
}

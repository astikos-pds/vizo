package br.app.vizo.core.report;

import br.app.vizo.core.report.exception.TooManyImagesException;
import br.app.vizo.core.shared.Image;

import java.util.Set;
import java.util.stream.Collectors;

public class EvidenceImages {

    private final Set<Image> images;

    public EvidenceImages(Set<Image> images) {
        if (images == null || images.size() > 5) {
            throw new TooManyImagesException();
        }
        this.images = images;
    }

    public static EvidenceImages of(Set<String> urls) {
        return new EvidenceImages(urls.stream().map(Image::new).collect(Collectors.toSet()));
    }

    public Set<String> getUrls() {
        return images.stream().map(Image::url).collect(Collectors.toSet());
    }
}

package br.app.vizo.core.shared;

public class Media {

    private final String url;

    public Media() {
        this("");
    }

    public Media(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

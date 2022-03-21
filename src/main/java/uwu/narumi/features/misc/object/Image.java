package uwu.narumi.features.misc.object;

public class Image {

    private final String url;
    private final String source;

    public Image(String url, String source) {
        this.url = url;
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }
}

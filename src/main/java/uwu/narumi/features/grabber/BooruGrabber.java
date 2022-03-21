package uwu.narumi.features.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uwu.narumi.features.misc.object.Image;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public interface BooruGrabber {

    default Elements fetchElements(String url, String tags) {
        try {
            return Jsoup.connect(String.format(url, URLEncoder.encode(tags, StandardCharsets.UTF_8))).get().getElementsByTag("post");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default Optional<Image> fetchImage(int position, Elements elements) {
        if (elements.size() <= position)
            return Optional.empty();

        Element post = elements.get(position);
        if (post == null)
            return Optional.empty();

        return Optional.of(new Image(post.attr("file_url"), post.attr("source")));
    }
}

package uwu.narumi.features.grabber;

import uwu.narumi.features.misc.object.Image;
import uwu.narumi.tama.helper.network.SiteHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public interface NekosLifeGrabber {

    String IMG_URL = "https://nekos.life/api/v2/img/%s";
    Set<String> IMG_ENDPOINTS = new HashSet<>(Arrays.asList("femdom", "tickle", "classic", "ngif",
            "erofeet", "meow", "erok", "poke", "les", "v3", "hololewd", "nekoapi_v3.1", "lewdk", "keta",
            "feetg", "nsfw_neko_gif", "eroyuri", "kiss", "8ball", "kuni", "tits", "pussy_jpg", "cum_jpg",
            "pussy", "lewdkemo", "lizard", "slap", "lewd", "cum", "cuddle", "spank", "smallboobs",
            "goose", "Random_hentai_gif", "avatar", "fox_girl", "nsfw_avatar", "hug", "gecg", "boobs",
            "pat", "feet", "smug", "kemonomimi", "solog", "holo", "wallpaper", "bj", "woof", "yuri",
            "trap", "anal", "baka", "blowjob", "holoero", "feed", "neko", "gasm", "hentai", "futanari",
            "ero", "solo", "waifu", "pwankg", "eron", "erokemo"));

    default Optional<Image> fetchImage(String endpoint) {
        try {
            return Optional.of(new Image(SiteHelper.toJson(String.format(IMG_URL, endpoint)).getString("url"), ""));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

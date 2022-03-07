package uwu.narumi.tama.helper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public final class SiteHelper {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/536.30 (KHTML, like Gecko) Chrome/95.0.4665.100 Safari/536.30";

    private SiteHelper() {
    }

    public static URLConnection openConnection(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String getUserAgent() {
        return USER_AGENT;
    }
}

package uwu.narumi.tama.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class BinHelper {

    private BinHelper() {
    }

    //Thanks "michal" for it
    public static String post(String text) {
        HttpURLConnection connection = null;
        String line;

        try {
            byte[] postData = text.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            connection = (HttpURLConnection) new URL("https://hastebin.com/documents").openConnection();

            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Hastebin Java Api");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            connection.setUseCaches(false);

            connection.getOutputStream().write(postData);
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                line = bufferedReader.readLine();
                line = line.substring(line.indexOf(":") + 2, line.lastIndexOf('"'));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return "https://hastebin.com/raw/" + line;
    }
}

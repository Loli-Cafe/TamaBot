package uwu.narumi.i18n;

import net.dv8tion.jda.internal.JDAImpl;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

//Some cheap localization xd
public class I18n {

    private static final Map<String, Properties> LOCALES = new ConcurrentHashMap<>();

    public static void load(String dir, String prefix) {

        try {
            LOCALES.clear();
            Files.list(Paths.get(dir))
                    .filter(path -> path.getFileName().toString().startsWith(prefix))
                    .filter(path -> path.getFileName().toString().endsWith(".properties"))
                    .forEach(path -> {
                        String language = stripLanguage(path.toFile().getName());
                        try (Reader reader = new FileReader(path.toFile())) {
                            Properties properties = new Properties();
                            properties.load(reader);

                            LOCALES.put(language.toLowerCase(Locale.ROOT), properties);
                        } catch (Exception e) {
                            JDAImpl.LOG.error("Can't load localization file {}", path, e);
                        }
                    });
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't load files from {}", dir, e);
        }

        JDAImpl.LOG.info("Loaded localization: {}", LOCALES.keySet());
    }

    public static String map(String language, String key) {
        language = language.toLowerCase(Locale.ROOT);

        if (!LOCALES.containsKey(language))
            return LOCALES.get("en").getProperty(key);

        return LOCALES.get(language).getProperty(key);
    }

    private static String stripLanguage(String string) {
        if (string.indexOf('_') == -1)
            return "en";

        String code = string.substring(string.indexOf('_') + 1, string.lastIndexOf('.'));
        if (code.indexOf('_') != -1)
            code = code.substring(0, code.indexOf('_'));

        return code;
    }

    public static boolean hasLanguage(String language) {
        return LOCALES.containsKey(language);
    }
}

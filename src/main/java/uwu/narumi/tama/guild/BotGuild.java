package uwu.narumi.tama.guild;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BotGuild {

    private final Set<String> whitelistedCommandsChannels = new HashSet<>();
    private final Set<String> whitelistedProxyChannels = new HashSet<>();
    private final Set<String> blacklistedLevelingChannels = new HashSet<>();

    private final Map<Integer, String> levelRewards = new HashMap<>();

    private String id;
    private String prefix = "l!";
    private String language;
    private boolean leveling;
    private boolean proxy;

    private String notificationChannel;

    public BotGuild() {
    }

    public BotGuild(String id) {
        this.id = id;
    }

    public Set<String> getWhitelistedCommandsChannels() {
        return whitelistedCommandsChannels;
    }

    public Set<String> getWhitelistedProxyChannels() {
        return whitelistedProxyChannels;
    }

    public Set<String> getBlacklistedLevelingChannels() {
        return blacklistedLevelingChannels;
    }

    public Map<Integer, String> getLevelRewards() {
        return levelRewards;
    }

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isLeveling() {
        return leveling;
    }

    public void setLeveling(boolean leveling) {
        this.leveling = leveling;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    public String getNotificationChannel() {
        return notificationChannel;
    }

    public void setNotificationChannel(String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }
}

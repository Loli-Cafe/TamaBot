package uwu.narumi.tama.database;

import uwu.narumi.tama.guild.BotGuild;
import uwu.narumi.tama.user.impl.GuildUser;
import uwu.narumi.tama.user.impl.SharedUser;

import java.util.List;

public interface DataBase {

    DataBase connect();

    void initialize();

    void saveAll();

    boolean isConnected();

    void disconnect();

    void createTable(String name);

    default boolean hasGuild(String guildId) {
        return hasGuildUsers(guildId) && hasGuildData(guildId);
    }

    boolean hasGuildUsers(String guildId);

    boolean hasGuildData(String guildId);

    boolean hasSharedUser(String userId);

    boolean hasGuildUser(String guildId, String userId);

    void saveGuild(BotGuild botGuild);

    void saveSharedUser(SharedUser sharedUser);

    void saveGuildUser(String guildId, GuildUser guildUser);

    BotGuild findGuild(String guildId);

    SharedUser findSharedUser(String userId);

    GuildUser findGuildUser(String guildId, String userId);

    List<GuildUser> fetchAll(String guildId);
}

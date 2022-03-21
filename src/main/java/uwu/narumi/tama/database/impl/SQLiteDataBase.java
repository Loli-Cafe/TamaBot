package uwu.narumi.tama.database.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.internal.JDAImpl;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.database.DataBase;
import uwu.narumi.tama.guild.BotGuild;
import uwu.narumi.tama.user.impl.GuildUser;
import uwu.narumi.tama.user.impl.SharedUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SQLiteDataBase implements DataBase {

    private static final String URL = "jdbc:sqlite:%s";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final String databaseUrl;
    private final String usersTable;
    private final String guildsTable;

    private Connection connection;

    public SQLiteDataBase() {
        databaseUrl = Tama.INSTANCE.getDatabaseProperties().getProperty("url",
                String.format(URL, Tama.INSTANCE.getDatabaseProperties().getProperty("database.name", "TamaBot")));

        usersTable = Tama.INSTANCE.getDatabaseProperties().getProperty("database.users.table.name");
        guildsTable = Tama.INSTANCE.getDatabaseProperties().getProperty("database.guilds.table.name");
    }

    @Override
    public DataBase connect() {
        try {
            connection = DriverManager.getConnection(databaseUrl, Tama.INSTANCE.getDatabaseProperties().getProperty("user"), Tama.INSTANCE.getDatabaseProperties().getProperty("password"));
            JDAImpl.LOG.info("Connected to \"{}\" database", databaseUrl);

            initialize();
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't connect to \"{}\" database", databaseUrl, e);
            throw new IllegalStateException();
        }

        return this;
    }

    @Override
    public void initialize() {
        createTable(usersTable);
        createTable(guildsTable);

        JDAImpl.LOG.info("Initialized \"{}\" database", databaseUrl);
    }

    @Override
    public void saveAll() {
        try {
            JDAImpl.LOG.info("Saving data to \"{}\" database", databaseUrl);

            Tama.INSTANCE.getGuildManager().asMap().values().forEach(guild -> saveGuild(guild.join()));
            Tama.INSTANCE.getUserManager().guildUsersAsMap().forEach((identifier, user) -> saveGuildUser(identifier.getGuildId(), user.join()));
            Tama.INSTANCE.getUserManager().sharedUsersAsShaMap().values().forEach(user -> saveSharedUser(user.join()));

            JDAImpl.LOG.info("Saved data to \"{}\" database", databaseUrl);
        } catch (Exception e) {
            JDAImpl.LOG.info("Error occurred during saving data to \"{}\" database", databaseUrl);
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                JDAImpl.LOG.info("Disconnected from \"{}\" database", databaseUrl);
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't disconnect from \"{}\" database", databaseUrl, e);
        }
    }

    @Override
    public void createTable(String name) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS " + name + "(id TEXT PRIMARY KEY NOT NULL , data JSON NOT NULL)");
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't create \"{}\" table", name, e);
        }
    }

    @Override
    public boolean hasGuildUsers(String guildId) {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            try (ResultSet resultSet = databaseMetaData.getTables(null, null, "g" + guildId, null)) {
                return resultSet.next();
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "hasGuild", guildId, e);
        }

        return false;
    }

    @Override
    public boolean hasGuildData(String guildId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS (SELECT TRUE FROM " + guildsTable + " WHERE id=?)")) {
            statement.setString(1, guildId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean(1);
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "hasGuildData", guildId, e);
        }

        return false;
    }

    @Override
    public boolean hasSharedUser(String userId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS (SELECT TRUE FROM " + usersTable + " WHERE id=?)")) {
            statement.setString(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean(1);
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "hasSharedUser", userId, e);
        }

        return false;
    }

    @Override
    public boolean hasGuildUser(String guildId, String userId) {
        if (!hasGuild(guildId))
            return false;

        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS (SELECT TRUE FROM " + "g" + guildId + " WHERE id=?)")) {
            statement.setString(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean(1);
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "hasGuildUser", userId, e);
        }

        return false;
    }

    @Override
    public void saveGuild(BotGuild botGuild) {
        createTable("g" + botGuild.getId());
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + guildsTable + "(id, data) VALUES(?, ?) ON CONFLICT(id) DO UPDATE SET data=? WHERE id=?")) {
            String json = OBJECT_MAPPER.writeValueAsString(botGuild);

            statement.setString(1, botGuild.getId());
            statement.setString(2, json);
            statement.setString(3, json);
            statement.setString(4, botGuild.getId());

            statement.execute();
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "createGuild", botGuild.getId(), e);
        }
    }

    @Override
    public void saveSharedUser(SharedUser sharedUser) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + usersTable + "(id, data) VALUES(?, ?) ON CONFLICT(id) DO UPDATE SET data=? WHERE id=?")) {
            String json = OBJECT_MAPPER.writeValueAsString(sharedUser);

            statement.setString(1, sharedUser.getId());
            statement.setString(2, json);
            statement.setString(3, json);
            statement.setString(4, sharedUser.getId());

            statement.execute();
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "createSharedUser", sharedUser.getId(), e);
        }
    }

    @Override
    public void saveGuildUser(String guildId, GuildUser guildUser) {
        createTable("g" + guildId);
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + "g" + guildId + "(id, data) VALUES(?, ?) ON CONFLICT(id) DO UPDATE SET data=? WHERE id=?")) {
            String json = OBJECT_MAPPER.writeValueAsString(guildUser);

            statement.setString(1, guildUser.getId());
            statement.setString(2, json);
            statement.setString(3, json);
            statement.setString(4, guildUser.getId());

            statement.execute();
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{} | {}\"", "createGuildUser", guildId, guildUser.getId(), e);
        }
    }

    @Override
    public BotGuild findGuild(String guildId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, data FROM " + guildsTable + " WHERE id=?")) {
            statement.setString(1, guildId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return OBJECT_MAPPER.readValue(resultSet.getString("data"), BotGuild.class);
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "findGuild", guildId, e);
        }

        return null;
    }

    @Override
    public SharedUser findSharedUser(String userId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, data FROM " + userId + " WHERE id=?")) {
            statement.setString(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return OBJECT_MAPPER.readValue(resultSet.getString("data"), SharedUser.class);
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "findSharedUser", userId, e);
        }

        return null;
    }

    @Override
    public GuildUser findGuildUser(String guildId, String userId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, data FROM " + "g" + guildId + " WHERE id=?")) {
            statement.setString(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return OBJECT_MAPPER.readValue(resultSet.getString("data"), GuildUser.class);
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{} | {}\"", "findGuildUser", guildId, userId, e);
        }

        return null;
    }

    @Override
    public List<GuildUser> fetchAll(String guildId) {
        Set<GuildUser> cacheUsers = Tama.INSTANCE.getUserManager().asSet(guildId);
        List<GuildUser> users = new ArrayList<>();

        if (!hasGuild(guildId))
            return users;

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + "g" + guildId)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    if (cacheUsers.stream().anyMatch(guildUser -> guildUser.getId().equals(id)))
                        continue;

                    users.add(OBJECT_MAPPER.readValue(resultSet.getString("data"), GuildUser.class));
                }
            }
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't execute \"{}\" statement for \"{}\"", "fetchAll", guildId, e);
        }

        users.addAll(cacheUsers);
        return users;
    }
}

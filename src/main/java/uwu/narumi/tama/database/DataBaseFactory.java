package uwu.narumi.tama.database;

import net.dv8tion.jda.internal.JDAImpl;
import uwu.narumi.tama.database.impl.SQLiteDataBase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class DataBaseFactory {

    private static final Map<String, Class<? extends DataBase>> DATA_BASES = new HashMap<>() {{
        put("sqlite", SQLiteDataBase.class);
    }};

    private DataBaseFactory() {
    }

    public static Optional<DataBase> createInstance(String type) {
        try {
            return Optional.of(DATA_BASES.getOrDefault(type.toLowerCase(Locale.ROOT), SQLiteDataBase.class).getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            JDAImpl.LOG.error("Can't create database", e);
        }

        return Optional.empty();
    }

    public static void registerDataBase(String name, Class<? extends DataBase> clazz) {
        if (DATA_BASES.containsKey(name.toLowerCase(Locale.ROOT))) {
            JDAImpl.LOG.error("Database {} already exists, use \"replaceDataBase\" method", name);
            return;
        }
        DATA_BASES.put(name, clazz);
    }

    public static Class<? extends DataBase> replaceDataBase(String name, Class<? extends DataBase> clazz) {
        return DATA_BASES.replace(name.toLowerCase(Locale.ROOT), clazz);
    }
}
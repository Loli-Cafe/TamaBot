package uwu.narumi.tama.guild;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.google.errorprone.annotations.CheckReturnValue;
import uwu.narumi.tama.Tama;

import java.util.Locale;
import java.util.concurrent.*;

public class BotGuildManager {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(
            Integer.parseInt(Tama.INSTANCE.getProperties().getProperty("guild.manager.threads"))
    );

    private final AsyncLoadingCache<String, BotGuild> guilds = Caffeine.newBuilder()
            .expireAfterWrite(
                    Long.parseLong(Tama.INSTANCE.getProperties().getProperty("guild.manager.cache.time")),
                    TimeUnit.valueOf(Tama.INSTANCE.getProperties().getProperty("guild.manager.cache.time.unit").toUpperCase(Locale.ROOT))
            ).executor(EXECUTOR_SERVICE)
            .removalListener((RemovalListener<String, BotGuild>) (id, botGuild, removalCause)
                    -> Tama.INSTANCE.getDataBase().saveGuild(botGuild))
            .buildAsync((id, executor) -> CompletableFuture.supplyAsync(()
                    -> Tama.INSTANCE.getDataBase().findGuild(id), executor));

    public CompletableFuture<BotGuild> findGuild(String guildId) {
        return guilds.get(guildId);
    }

    public AsyncLoadingCache<String, BotGuild> getGuilds() {
        return guilds;
    }

    @CheckReturnValue
    public ConcurrentMap<String, CompletableFuture<BotGuild>> asMap() {
        return guilds.asMap();
    }
}

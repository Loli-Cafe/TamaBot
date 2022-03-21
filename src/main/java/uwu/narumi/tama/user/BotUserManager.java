package uwu.narumi.tama.user;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.google.errorprone.annotations.CheckReturnValue;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.user.identifier.Identifier;
import uwu.narumi.tama.user.impl.GuildUser;
import uwu.narumi.tama.user.impl.SharedUser;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BotUserManager {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(
            Integer.parseInt(Tama.INSTANCE.getProperties().getProperty("user.manager.threads"))
    );

    private final AsyncLoadingCache<String, SharedUser> sharedUsers = Caffeine.newBuilder()
            .expireAfterWrite(
                    Long.parseLong(Tama.INSTANCE.getProperties().getProperty("user.manager.cache.time")),
                    TimeUnit.valueOf(Tama.INSTANCE.getProperties().getProperty("user.manager.cache.time.unit").toUpperCase(Locale.ROOT))
            ).executor(EXECUTOR_SERVICE)
            .removalListener((RemovalListener<String, SharedUser>) (id, sharedUser, removalCause)
                    -> Tama.INSTANCE.getDataBase().saveSharedUser(Objects.requireNonNull(sharedUser)))
            .buildAsync((id, executor) -> CompletableFuture.supplyAsync(()
                    -> Tama.INSTANCE.getDataBase().findSharedUser(id), executor));

    private final AsyncLoadingCache<Identifier, GuildUser> guildUsers = Caffeine.newBuilder()
            .expireAfterWrite(
                    Long.parseLong(Tama.INSTANCE.getProperties().getProperty("user.manager.cache.time")),
                    TimeUnit.valueOf(Tama.INSTANCE.getProperties().getProperty("user.manager.cache.time.unit").toUpperCase(Locale.ROOT))
            ).executor(EXECUTOR_SERVICE)
            .removalListener((RemovalListener<Identifier, GuildUser>) (identifier, guildUser, removalCause)
                    -> Tama.INSTANCE.getDataBase().saveGuildUser(Objects.requireNonNull(identifier).getGuildId(), Objects.requireNonNull(guildUser)))
            .buildAsync((identifier, executor) -> CompletableFuture.supplyAsync(()
                    -> Tama.INSTANCE.getDataBase().findGuildUser(identifier.getGuildId(), identifier.getUserId()), executor));

    public CompletableFuture<SharedUser> findSharedUser(String userId) {
        return sharedUsers.get(userId);
    }

    public CompletableFuture<GuildUser> findGuildUser(String guildId, String userId) {
        return guildUsers.get(Identifier.of(guildId, userId));
    }

    public CompletableFuture<GuildUser> findGuildUser(Identifier identifier) {
        return guildUsers.get(identifier);
    }

    public AsyncLoadingCache<String, SharedUser> getSharedUsers() {
        return sharedUsers;
    }

    public AsyncLoadingCache<Identifier, GuildUser> getGuildUsers() {
        return guildUsers;
    }

    @CheckReturnValue
    public ConcurrentMap<String, CompletableFuture<SharedUser>> sharedUsersAsShaMap() {
        return getSharedUsers().asMap();
    }

    @CheckReturnValue
    public ConcurrentMap<Identifier, CompletableFuture<GuildUser>> guildUsersAsMap() {
        return guildUsers.asMap();
    }

    public List<GuildUser> asList(String guildId) {
        return guildUsersAsMap().values().stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public Set<GuildUser> asSet(String guildId) {
        return guildUsersAsMap().values().stream().map(CompletableFuture::join).collect(Collectors.toSet());
    }
}

package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.guild.BotGuild;
import uwu.narumi.tama.user.impl.GuildUser;
import uwu.narumi.tama.user.impl.SharedUser;

import java.util.concurrent.ForkJoinPool;

public class DatabaseListener extends ListenerAdapter {

    private final ForkJoinPool forkJoinPool = new ForkJoinPool(1);

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot())
            return;

        forkJoinPool.execute(() -> {
            if (!Tama.INSTANCE.getDataBase().hasGuild(event.getGuild().getId())) {
                Tama.INSTANCE.getDataBase().saveGuild(new BotGuild(event.getGuild().getId()));
            }

            if (event.isWebhookMessage() || event.getAuthor().isBot())
                return;

            if (!Tama.INSTANCE.getDataBase().hasSharedUser(event.getAuthor().getId())) {
                Tama.INSTANCE.getDataBase().saveSharedUser(new SharedUser(event.getAuthor().getId()));
            }

            if (!Tama.INSTANCE.getDataBase().hasGuildUser(event.getGuild().getId(), event.getAuthor().getId())) {
                Tama.INSTANCE.getDataBase().saveGuildUser(event.getGuild().getId(), new GuildUser(event.getAuthor().getId()));
            }
        });
    }
}

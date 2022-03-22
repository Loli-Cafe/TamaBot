package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.helper.ExecutorHelper;

import java.util.Objects;


public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot())
            return;

        Tama.INSTANCE.getGuildManager().findGuild(Objects.requireNonNull(event.getGuild()).getId()).thenAccept(guild -> {
            if (guild.isCommandsBlacklist() && !guild.getWhitelistedCommandsChannels().isEmpty() && !guild.getWhitelistedCommandsChannels().contains(event.getChannel().getId()))
                return;

            if (event.getMessage().getContentDisplay().startsWith(guild.getPrefix())) {
                ExecutorHelper.execute(() -> {
                    Tama.INSTANCE.getCommandManager().handle(guild.getPrefix(), event);
                    event.getMessage().delete().complete();
                });
            }
        });
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Tama.INSTANCE.getGuildManager().findGuild(Objects.requireNonNull(event.getGuild()).getId()).thenAccept(guild -> {
            if (guild.isCommandsBlacklist() && !guild.getWhitelistedCommandsChannels().isEmpty() && !guild.getWhitelistedCommandsChannels().contains(event.getChannel().getId())) {
                event.reply("").complete().deleteOriginal().queue();
                return;
            }

            ExecutorHelper.execute(() -> Tama.INSTANCE.getCommandManager().handle(event));
        });
    }
}

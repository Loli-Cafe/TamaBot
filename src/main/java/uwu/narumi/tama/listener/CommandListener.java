package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.helper.ExecutorHelper;


public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getMessage().getContentDisplay().startsWith(Tama.INSTANCE.getCommandManager().getPrefix()))
            return;

        ExecutorHelper.execute(() -> {
            Tama.INSTANCE.getCommandManager().handle(event);
            event.getMessage().delete().complete();
        });
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        ExecutorHelper.execute(() -> Tama.INSTANCE.getCommandManager().handle(event));
    }
}

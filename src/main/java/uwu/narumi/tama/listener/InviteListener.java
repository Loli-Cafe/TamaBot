package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class InviteListener extends ListenerAdapter {

    private static final Pattern DISCORD_INVITE_PATTERN = Pattern.compile("");

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot())
            return;

        if (DISCORD_INVITE_PATTERN.matcher(event.getMessage().getContentStripped()).matches()) {
            event.getMessage().delete().queue();
        }
    }
}

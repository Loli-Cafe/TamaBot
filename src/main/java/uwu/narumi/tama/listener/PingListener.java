package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.helper.EmbedHelper;


public class PingListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getMessage().isMentioned(Tama.INSTANCE.getJda().getSelfUser(), Message.MentionType.USER))
            return;

        event.getMessage().reply(EmbedHelper.getNani() + " なに？").queue();
    }
}

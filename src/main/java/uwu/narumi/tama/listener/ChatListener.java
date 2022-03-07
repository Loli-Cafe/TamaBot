package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uwu.narumi.tama.helper.EmbedHelper;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ChatListener extends ListenerAdapter {

    private static final Set<String> GREETINGS = new HashSet<>() {{
        add("hi");
        add("ohayou");
        add("konnichiwa");
        add("おはよう");
        add("こんにち");
        add("good morning");
    }};

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        String message = event.getMessage().getContentDisplay().toLowerCase(Locale.ROOT);
        for (String greeting : GREETINGS) {
            for (String part : message.split(" ")) {
                if (part.equals(greeting)) {
                    event.getMessage().reply(EmbedHelper.getHi() + " おはよう！").queue();
                    break;
                }
            }
        }
    }
}

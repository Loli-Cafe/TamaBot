package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/*
    TODO: Better system to handle this
 */
public class ChatListener extends ListenerAdapter {

    private static final Set<String> GREETINGS = new HashSet<>() {{
        add("hi");
        add("hii");
        add("ohayou");
        add("konnichiwa");
        add("おはよう");
        add("こんにち");
        add("こんばんは");
        add("konbanwa");
        add("good morning");
        add("goodmorning");
        add("morning");
        add("i am back");
        add("i'm back");
        add("sup");
        add("hello");
        //add("hey");
        add("howdy");
        //add("yo");
    }};

    private static final Set<String> FAREWELLS = new HashSet<>() {{
        add("bye");
        add("こんばんは");
        add("konbanwa");
        add("goodbye");
        add("goodnight");
        add("good night");
        add("g night");
        add("gnight");
        //add("gn");
    }};

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot())
            return;

        String message = event.getMessage().getContentDisplay().toLowerCase(Locale.ROOT);
        for (String greeting : GREETINGS) {
            if (message.startsWith(greeting)) {
                event.getMessage().reply(EmbedHelper.getHi() + " Hello!").queue();
                break;
            }

            for (String part : message.split(" ")) {
                if (part.equals(greeting)) {
                    event.getMessage().reply(EmbedHelper.getHi() + " Hello!").queue();
                    break;
                }
            }
        }

        for (String farewell : FAREWELLS) {
            if (message.startsWith(farewell)) {
                event.getMessage().reply(EmbedHelper.getHi() + " Goodnight!").queue();
                break;
            }

            for (String part : message.split(" ")) {
                if (part.equals(farewell)) {
                    event.getMessage().reply(EmbedHelper.getHi() + " Goodnight!").queue();
                    break;
                }
            }
        }
    }
}

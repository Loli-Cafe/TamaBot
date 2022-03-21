package uwu.narumi.tama.helper.discord;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

/*
    Idk why i created it
 */
public final class DiscordHelper {

    private DiscordHelper() {
    }

    public static String asMention(User user) {
        return user.getAsMention();
    }

    public static String asMention(Member member) {
        return member.getUser().getAsMention();
    }

    public static String asMentionUser(MessageReceivedEvent event) {
        return event.getAuthor().getAsMention();
    }

    public static boolean hasMention(MessageReceivedEvent event, Message.MentionType mentionType) {
        return !event.getMessage().getMentions(mentionType).isEmpty();
    }

    public static List<IMentionable> getMentions(MessageReceivedEvent event, Message.MentionType mentionType) {
        return event.getMessage().getMentions(mentionType);
    }

    public static IMentionable getMention(MessageReceivedEvent event, Message.MentionType mentionType) {
        return getMention(event, mentionType, 0);
    }

    public static IMentionable getMention(MessageReceivedEvent event, Message.MentionType mentionType, int pos) {
        return getMentions(event, mentionType).get(pos);
    }
}

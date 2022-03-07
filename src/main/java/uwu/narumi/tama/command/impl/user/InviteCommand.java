package uwu.narumi.tama.command.impl.user;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.EmbedHelper;

import java.util.concurrent.TimeUnit;

@CommandInfo(
        alias = "invite",
        description = "Send TamaBot invite",
        usage = "/invite",
        type = CommandType.USER,
        aliases = "bot"
)
public class InviteCommand extends Command {

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        event.getTextChannel().sendMessageEmbeds(execute(event.getAuthor())).complete().delete().completeAfter(15, TimeUnit.SECONDS);
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        event.replyEmbeds(execute(event.getUser())).queue();
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        return false;
    }

    @Override
    public MessageEmbed execute(Object... args) {
        return EmbedHelper.invite((User) args[0]);
    }
}

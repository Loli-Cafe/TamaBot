package uwu.narumi.tama.command.impl.anime;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uwu.narumi.features.grabber.NekosLifeGrabber;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandException;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.EmbedHelper;

@CommandInfo(
        alias = "smug",
        description = "?",
        usage = "/smug",
        type = CommandType.ANIME
)
public class SmugCommand extends Command implements NekosLifeGrabber {

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        event.getTextChannel().sendMessageEmbeds(execute(event.getAuthor().getAsMention() + " is smug!")).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        event.replyEmbeds(execute(event.getUser().getAsMention() + " is smug!")).queue();
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        return false;
    }

    @Override
    public MessageEmbed execute(Object... args) {
        return EmbedHelper.action((String) args[0], fetchImage("smug").orElseThrow(() -> new CommandException("Something went wrong")));
    }
}

package uwu.narumi.tama.command.impl.nsfw.rp;

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
        alias = "prostitute",
        description = "?",
        usage = "/prostitute",
        type = CommandType.NSFW
)
public class ProstituteCommand extends Command implements NekosLifeGrabber {

    private static final String URL = "https://img1.gelbooru.com//images/6f/d4/6fd4ce862fa14904d0b099f8ed3402a6.gif";

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        checkContext(event.getMember(), event.getTextChannel());
        event.getTextChannel().sendMessageEmbeds(execute(String.format("%s prostituted yourself", event.getAuthor().getAsMention()))).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        checkContext(event.getMember(), event.getTextChannel());
        event.replyEmbeds(execute(String.format("%s prostituted yourself", event.getUser().getAsMention()))).queue();
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        if (!textChannel.isNSFW())
            throw new CommandException("Channel isn't marked as NSFW");

        return true;
    }

    @Override
    public MessageEmbed execute(Object... args) {
        return EmbedHelper.action((String) args[0], URL);
    }
}

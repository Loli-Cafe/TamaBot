package uwu.narumi.tama.command.impl.nsfw.rp;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import uwu.narumi.features.grabber.NekosLifeGrabber;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandException;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.DiscordHelper;
import uwu.narumi.tama.helper.discord.EmbedHelper;

@CommandInfo(
        alias = "slap",
        description = "?",
        usage = "/slap [user (mention)]",
        type = CommandType.ANIME
)
public class SlapCommand extends Command implements NekosLifeGrabber {

    private static final String URL = "https://img3.gelbooru.com//images/b2/09/b209bf140ec675ec27294ad8b158477b.gif";

    public SlapCommand() {
        super(
                new OptionData(OptionType.USER, "user", "?", false, false)
        );
    }

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        checkContext(event.getMember(), event.getTextChannel());
        event.getTextChannel().sendMessageEmbeds(execute(
                DiscordHelper.hasMention(event, Message.MentionType.USER)
                        ? String.format("%s slaps %s", event.getAuthor(), DiscordHelper.getMention(event, Message.MentionType.USER).getAsMention())
                        : (event.getAuthor().getAsMention() + " would like to slapped")
        )).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        checkContext(event.getMember(), event.getTextChannel());
        OptionMapping optionMapping = event.getOption("user");

        event.replyEmbeds(execute(
                optionMapping != null
                        ? String.format("%s slaps %s", event.getUser(), optionMapping.getAsUser().getAsMention())
                        : (event.getUser().getAsMention() + " would like to slapped")
        )).queue();
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

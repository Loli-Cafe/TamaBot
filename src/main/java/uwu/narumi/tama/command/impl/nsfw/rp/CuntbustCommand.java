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
        alias = "cuntbust",
        description = "?",
        usage = "/cuntbust <person (mention)>",
        type = CommandType.NSFW,
        argsLength = 1
)
public class CuntbustCommand extends Command implements NekosLifeGrabber {

    private static final String URL = "https://i5.nhentai.net/galleries/2136685/97.jpg";

    public CuntbustCommand() {
        super(
                new OptionData(OptionType.USER, "user", "?", true, false)
        );
    }

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        checkContext(event.getMember(), event.getTextChannel());
        if (!DiscordHelper.hasMention(event, Message.MentionType.USER))
            throw new CommandException(getUsage());
        event.getTextChannel().sendMessageEmbeds(execute(
                String.format("%s cuntbusted %s", event.getAuthor().getAsMention(), DiscordHelper.getMention(event, Message.MentionType.USER).getAsMention())
        )).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        checkContext(event.getMember(), event.getTextChannel());
        OptionMapping optionMapping = event.getOption("user");
        if (optionMapping == null)
            throw new CommandException(getUsage());

        event.replyEmbeds(execute(
                String.format("%s cuntbusted %s", event.getUser().getAsMention(), optionMapping.getAsUser().getAsMention())
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

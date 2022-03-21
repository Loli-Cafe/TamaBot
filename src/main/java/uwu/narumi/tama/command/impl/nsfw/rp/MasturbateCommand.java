package uwu.narumi.tama.command.impl.nsfw.rp;

import net.dv8tion.jda.api.entities.Member;
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
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.Objects;

@CommandInfo(
        alias = "masturbate",
        description = "?",
        usage = "/masturbate <dick/pussy>",
        type = CommandType.NSFW,
        argsLength = 1
)
public class MasturbateCommand extends Command implements NekosLifeGrabber {

    private static final String MALE_URL = "https://img1.gelbooru.com//images/81/0f/810f437c57e2134c3e2a868fef379c26.gif";
    private static final String FEMALE_URL = "https://img1.gelbooru.com//images/4a/4f/4a4ff385cf54310ce7b6e01ac95070ef.gif";

    public MasturbateCommand() {
        super(
                new OptionData(OptionType.STRING, "type", "?", true, false)
        );
    }

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        checkContext(event.getMember(), event.getTextChannel());

        event.getTextChannel().sendMessageEmbeds(execute(
                String.format("%s masturbating", event.getAuthor().getAsMention()),
                args[0]
        )).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        checkContext(event.getMember(), event.getTextChannel());
        OptionMapping optionMapping = event.getOption("type");
        event.replyEmbeds(execute(
                String.format("%s masturbating", event.getUser().getAsMention()),
                Objects.requireNonNull(optionMapping).getAsString()
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
        return EmbedHelper.action((String) args[0], args[1].equals("dick") ? MALE_URL : FEMALE_URL);
    }
}

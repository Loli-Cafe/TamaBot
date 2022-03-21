package uwu.narumi.tama.command.impl.anime;

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
        alias = "baka",
        description = "?",
        usage = "/baka <person (mention)>",
        type = CommandType.ANIME,
        argsLength = 1
)
public class BakaCommand extends Command implements NekosLifeGrabber {

    public BakaCommand() {
        super(
                new OptionData(OptionType.USER, "user", "Someone who is baka", true, false)
        );
    }

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        if (!DiscordHelper.hasMention(event, Message.MentionType.USER))
            throw new CommandException(getUsage());

        event.getTextChannel().sendMessageEmbeds(execute(
                String.format("%s said %s is baka", event.getAuthor().getAsMention(), DiscordHelper.getMention(event, Message.MentionType.USER).getAsMention())
        )).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        OptionMapping optionMapping = event.getOption("user");
        if (optionMapping == null)
            throw new CommandException(getUsage());

        event.replyEmbeds(execute(
                String.format("%s said %s is baka", event.getUser().getAsMention(), optionMapping.getAsUser().getAsMention())
        )).queue();
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        return false;
    }

    @Override
    public MessageEmbed execute(Object... args) {
        return EmbedHelper.action((String) args[0], fetchImage("baka").orElseThrow(() -> new CommandException("Something went wrong")));
    }
}

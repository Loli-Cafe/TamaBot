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
        alias = "kiss",
        description = "?",
        usage = "/kiss [user (mention)]",
        type = CommandType.ANIME
)
public class KissCommand extends Command implements NekosLifeGrabber {

    public KissCommand() {
        super(
                new OptionData(OptionType.USER, "user", "Someone who you want to kiss", false, false)
        );
    }

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        event.getTextChannel().sendMessageEmbeds(execute(
                DiscordHelper.hasMention(event, Message.MentionType.USER)
                        ? String.format("%s kissed %s", event.getAuthor(), DiscordHelper.getMention(event, Message.MentionType.USER).getAsMention())
                        : (event.getAuthor().getAsMention() + " expects kiss")
        )).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        OptionMapping optionMapping = event.getOption("user");

        event.replyEmbeds(execute(
                optionMapping != null
                        ? String.format("%s kissed %s", event.getUser(), optionMapping.getAsUser().getAsMention())
                        : (event.getUser().getAsMention() + " expects kiss")
        )).queue();
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        return false;
    }

    @Override
    public MessageEmbed execute(Object... args) {
        return EmbedHelper.action((String) args[0], fetchImage("kiss").orElseThrow(() -> new CommandException("Something went wrong")));
    }
}
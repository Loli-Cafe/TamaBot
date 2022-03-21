package uwu.narumi.tama.command.impl.nsfw;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import uwu.narumi.features.grabber.NekosLifeGrabber;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandException;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.Locale;

@CommandInfo(
        alias = "nekoslife",
        description = "Shows random image from nekoslife",
        usage = "/nekoslife [tag]",
        type = CommandType.NSFW,
        aliases = "neko"
)
public class NekosLifeCommand extends Command implements NekosLifeGrabber {

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        event.getTextChannel().sendMessageEmbeds(execute(args.length > 0 ? args[0] : "lewd", event.getAuthor())).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        OptionMapping optionType = event.getOption("tag");
        event.replyEmbeds(execute(optionType != null ? optionType.getAsString() : "lewd", event.getUser())).queue();
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        if (!textChannel.isNSFW())
            throw new CommandException("Channel isn't marked as NSFW");

        if (!getType().canInvoke(member))
            throw new CommandException("You doesn't have permission to invoke this command.");

        return true;
    }

    @Override
    public MessageEmbed execute(Object... args) {
        String tag = ((String) args[0]).toLowerCase(Locale.ROOT);
        if (tag.equals("list")) {
            return EmbedHelper.info("Available Tags", String.join(", ", IMG_ENDPOINTS));
        } else if (!IMG_ENDPOINTS.contains(tag)) {
            return EmbedHelper.error(String.format("Tag \"%s\" doesn't exists", tag));
        } else {
            return EmbedHelper.image((User) args[1], tag,
                    fetchImage(tag).orElseThrow(
                            () -> new CommandException("Can't fetch image")));
        }
    }
}

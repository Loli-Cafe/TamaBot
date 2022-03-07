package uwu.narumi.tama.command.impl.anime;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jsoup.select.Elements;
import uwu.narumi.anime.booru.TagsParser;
import uwu.narumi.anime.grabber.BooruGrabber;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandException;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.EmbedHelper;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@CommandInfo(
        alias = "yandere",
        description = "Shows random image from yandere",
        usage = "/yandere [tags] [rating] | /yandere $search",
        type = CommandType.ANIME,
        aliases = "yr"
)
public class YandereCommand extends Command implements BooruGrabber {

    private final String url = "https://yande.re/post.xml?" +
            "page=0" +
            "&limit=100" +
            "&tags=%s";

    public YandereCommand() {
        super(
                new OptionData(OptionType.STRING, "tags", "Image tags", false, false),
                new OptionData(OptionType.STRING, "rating", "Rating of image", false, false),
                new OptionData(OptionType.STRING, "search", "Search", false, false)
        );
    }

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        if (!checkContext(event.getMember(), event.getTextChannel()))
            return;

        //ugly
        String tags = args.length > 0 && args[0].startsWith("$") ? String.join(" ", args).substring(1) : TagsParser.parse(
                args.length > 1 ? args[args.length - 1] : "e",
                args.length > 2 ? Arrays.copyOfRange(args, 0, args.length - 1) : args.length == 1 ? new String[]{args[0]} : new String[]{"loli"}
        );

        event.getTextChannel().sendMessageEmbeds(execute(
                tags,
                event.getAuthor()
        )).queue();
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        OptionMapping tags = event.getOption("tags");
        OptionMapping rating = event.getOption("rating");
        OptionMapping search = event.getOption("search");

        if (search != null) {
            event.replyEmbeds(execute(search.getAsString(), event.getUser())).queue();
        } else {
            event.replyEmbeds(execute(TagsParser.parse(
                    rating != null ? rating.getAsString() : "e",
                    tags != null ? tags.getAsString().split(" ") : new String[]{"loli"}
            ), event.getUser())).queue();
        }
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
        Elements elements = fetchElements(url, (String) args[0]);
        if (elements == null)
            throw new CommandException("Something went wrong");

        if (elements.size() <= 0)
            throw new CommandException("Not images found, with specified tags");

        AtomicReference<MessageEmbed> messageEmbed = new AtomicReference<>();
        fetchImage(ThreadLocalRandom.current().nextInt(elements.size()), elements).ifPresentOrElse(image ->
                        messageEmbed.set(EmbedHelper.image((User) args[1], ((String) args[0]).replace("+", ", "), image)),
                () -> messageEmbed.set(EmbedHelper.error("Could not fetch image")));

        return messageEmbed.get();
    }
}
package uwu.narumi.tama.command.impl.nsfw;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uwu.narumi.features.grabber.BooruGrabber;
import uwu.narumi.features.misc.object.Image;
import uwu.narumi.features.misc.parser.TagsParser;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandException;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@CommandInfo(
        alias = "gelbooru",
        description = "Shows random image from gelbooru",
        usage = "/gelbooru [tags] [rating] | /gelbooru $search",
        type = CommandType.NSFW,
        aliases = "gb"
)
public class GelbooruCommand extends Command implements BooruGrabber {

    private final String url = ("https://gelbooru.com/index.php?page=dapi&s=post&q=index" +
            "&pid=0" +
            "&limit=100" +
            "&tags=%s" +
            "&api_key={api_key}" +
            "&user_id={user_id}")
            .replace("{api_key}", Tama.INSTANCE.getProperties().getProperty("grabber.gelbooru.api_key"))
            .replace("{user_id}", Tama.INSTANCE.getProperties().getProperty("grabber.gelbooru.user_id"));

    public GelbooruCommand() {
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

        return EmbedHelper.image((User) args[1], ((String) args[0]).replace("+", ", "),
                fetchImage(ThreadLocalRandom.current().nextInt(elements.size()), elements).orElseThrow(
                        () -> new CommandException("Could not fetch image")));
    }

    @Override
    public Optional<Image> fetchImage(int position, Elements elements) {
        if (elements.size() <= position)
            return Optional.empty();

        Element post = elements.get(position);
        if (post == null)
            return Optional.empty();

        return Optional.of(new Image(post.select("file_url").text(), post.select("source").text()));
    }
}

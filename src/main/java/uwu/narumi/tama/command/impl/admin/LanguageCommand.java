package uwu.narumi.tama.command.impl.admin;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import uwu.narumi.i18n.I18n;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandException;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.Objects;

@CommandInfo(
        alias = "language",
        description = "Sets TamaBot language",
        usage = "/language <language>",
        type = CommandType.ADMIN,
        argsLength = 1
)
public class LanguageCommand extends Command {

    public LanguageCommand() {
        super(
                new OptionData(OptionType.STRING, "language", "New bot language", true, false)
        );
    }

    /*
        TODO: Make better handling for this type of commands
     */
    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        checkContext(event.getMember(), event.getTextChannel());

        Tama.INSTANCE.getGuildManager().findGuild(Objects.requireNonNull(event.getGuild()).getId()).thenAccept(guild -> {
            if (guild == null)
                handleException(event, new CommandException("Can't fetch guild"));

            if (!I18n.hasLanguage(args[0]))
                handleException(event, new CommandException(String.format("Language %s not found", args[0])));

            guild.setLanguage(args[0]);
            event.getTextChannel().sendMessageEmbeds(EmbedHelper.success("TamaBot language was set to: " + args[0])).queue();
        });
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        checkContext(event.getMember(), event.getTextChannel());

        Tama.INSTANCE.getGuildManager().findGuild(Objects.requireNonNull(event.getGuild()).getId()).thenAccept(guild -> {
            if (guild == null)
                handleException(event, new CommandException("Can't fetch guild"));

            String language = Objects.requireNonNull(event.getOption("language")).getAsString();
            if (!I18n.hasLanguage(language))
                handleException(event, new CommandException(String.format("Language %s not found", language)));

            guild.setLanguage(language);
            event.getTextChannel().sendMessageEmbeds(EmbedHelper.success("TamaBot language was set to: " + language)).queue();
        });
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        if (!getType().canInvoke(member))
            throw new CommandException("You don't have permission to invoke this command");

        return false;
    }

    @Override
    public MessageEmbed execute(Object... args) {
        throw new UnsupportedOperationException();
    }
}

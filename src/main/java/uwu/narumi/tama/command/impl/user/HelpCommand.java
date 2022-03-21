package uwu.narumi.tama.command.impl.user;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@CommandInfo(
        alias = "help",
        description = "Commands",
        usage = "/help [command]",
        type = CommandType.USER
)
public class HelpCommand extends Command {

    public HelpCommand() {
        super(
                new OptionData(OptionType.STRING, "command", "Command name", false, false)
        );
    }

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        event.getAuthor().openPrivateChannel().complete().sendMessageEmbeds(execute(event.getAuthor(), args.length > 0 ? args[0] : null)).complete().delete().completeAfter(30, TimeUnit.SECONDS);
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        OptionMapping command = event.getOption("command");
        event.getUser().openPrivateChannel().complete().sendMessageEmbeds(execute(event.getUser(), command != null ? command.getAsString() : null)).complete().delete().completeAfter(30, TimeUnit.SECONDS);
        event.deferReply().queue();
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        return false;
    }

    @Override
    public MessageEmbed execute(Object... args) {
        if (args.length > 1 && args[1] instanceof String) { //i think i should recode it
            AtomicReference<MessageEmbed> messageEmbed = new AtomicReference<>();
            Tama.INSTANCE.getCommandManager().findCommand((String) args[1]).ifPresentOrElse(command -> messageEmbed.set(EmbedHelper.command((User) args[0], command)), () -> messageEmbed.set(EmbedHelper.help((User) args[0], Tama.INSTANCE.getCommandManager().getCommands().stream().filter(command -> !(command instanceof HelpCommand)).collect(Collectors.toList()))));

            return messageEmbed.get();
        } else {
            return EmbedHelper.help((User) args[0], Tama.INSTANCE.getCommandManager().getCommands().stream().filter(command -> !(command instanceof HelpCommand)).collect(Collectors.toList()));
        }
    }
}

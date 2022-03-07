package uwu.narumi.tama.command;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.helper.BinHelper;
import uwu.narumi.tama.helper.EmbedHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CommandManager {

    private final String prefix;
    private final List<Command> commands;

    public CommandManager(Command... commands) {
        this.prefix = Tama.INSTANCE.getProperties().getProperty("discord.bot.command.prefix");
        this.commands = List.of(commands);
    }

    public void handle(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay().substring(prefix.length());
        if (message.isBlank() || message.isEmpty())
            return;

        String[] args = message.split(" ");
        findCommand(args[0]).ifPresentOrElse(command -> {
            try {
                if (args.length - 1 < command.getArgsLength())
                    throw new CommandException("Usage: " + command.getUsage());

                command.compose(event, Arrays.copyOfRange(args, 1, args.length));
            } catch (Exception e) {
                handleException(event.getTextChannel(), e);
            }
        }, () -> event.getChannel().sendMessageEmbeds(EmbedHelper.error(String.format("Command \"%s\" not found!", args[0]))).complete().delete().completeAfter(10, TimeUnit.SECONDS));
    }

    public void handle(SlashCommandInteractionEvent event) {
        findCommand(event.getName()).ifPresentOrElse(command -> {
            try {
                if (event.getOptions().size() < command.getArgsLength())
                    throw new CommandException("Usage: " + command.getUsage());

                command.compose(event);
            } catch (Exception e) {
                handleException(event.getTextChannel(), e);
            }
        }, () -> event.replyEmbeds(EmbedHelper.error(String.format("Command \"%s\" not found!", event.getName()))).queue());
    }

    private void handleException(TextChannel textChannel, Exception exception) {
        if (exception instanceof CommandException) {
            textChannel.sendMessageEmbeds(EmbedHelper.error(exception.getMessage())).complete().delete().completeAfter(10, TimeUnit.SECONDS);
        } else {
            textChannel.sendMessageEmbeds(EmbedHelper.error(BinHelper.post(ExceptionUtils.getStackTrace(exception)))).complete().delete().completeAfter(20, TimeUnit.SECONDS);
        }
    }

    public Optional<Command> findCommand(String commandName) {
        return commands.stream().filter(command -> command.match(commandName)).findFirst();
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getPrefix() {
        return prefix;
    }
}

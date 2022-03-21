package uwu.narumi.tama.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.helper.ExceptionHelper;
import uwu.narumi.tama.helper.discord.EmbedHelper;
import uwu.narumi.tama.helper.network.BinHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CommandManager {

    private final String prefix;
    private final List<Command> commands = new ArrayList<>();

    public CommandManager(Command... commands) {
        this.prefix = Tama.INSTANCE.getProperties().getProperty("discord.bot.command.prefix");
        registerCommands(commands);
    }

    public void handle(String prefix, MessageReceivedEvent event) {
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
                handleException(event, e);
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
                handleException(event, e);
            }
        }, () -> event.replyEmbeds(EmbedHelper.error(String.format("Command \"%s\" not found!", event.getName()))).queue());
    }

    public void handleException(MessageReceivedEvent event, Exception exception) {
        if (exception instanceof CommandException) {
            event.getTextChannel().sendMessageEmbeds(EmbedHelper.error(exception.getMessage())).complete().delete().completeAfter(10, TimeUnit.SECONDS);
        } else {
            event.getTextChannel().sendMessageEmbeds(EmbedHelper.error(BinHelper.post("Error occurred: " + ExceptionHelper.toString(exception)))).complete().delete().completeAfter(20, TimeUnit.SECONDS);
        }
    }

    public void handleException(SlashCommandInteractionEvent event, Exception exception) {
        if (exception instanceof CommandException) {
            event.replyEmbeds(EmbedHelper.error(exception.getMessage())).complete().deleteOriginal().completeAfter(10, TimeUnit.SECONDS);
        } else {
            event.replyEmbeds(EmbedHelper.error(BinHelper.post("Error occurred: " + ExceptionHelper.toString(exception)))).complete().deleteOriginal().completeAfter(20, TimeUnit.SECONDS);
        }
    }

    public Optional<Command> findCommand(String commandName) {
        return commands.stream().filter(command -> command.match(commandName)).findFirst();
    }

    public void registerCommands(Command... commands) {
        this.commands.addAll(List.of(commands));
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getPrefix() {
        return prefix;
    }
}

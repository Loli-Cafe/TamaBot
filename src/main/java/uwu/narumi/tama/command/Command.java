package uwu.narumi.tama.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import uwu.narumi.tama.Tama;

import java.util.List;

public abstract class Command {

    private final String alias;
    private final String description;
    private final String usage;
    private final CommandType type;
    private final String[] aliases;
    private final int argsLength;
    private final List<OptionData> options;

    public Command() {
        this(new OptionData[0]);
    }

    public Command(OptionData... options) {
        CommandInfo commandInfo = this.getClass().getDeclaredAnnotation(CommandInfo.class);
        if (commandInfo == null)
            throw new IllegalStateException();

        alias = commandInfo.alias();
        description = commandInfo.description();
        usage = commandInfo.usage();
        type = commandInfo.type();
        aliases = commandInfo.aliases();
        argsLength = commandInfo.argsLength();

        this.options = List.of(options);
    }

    public abstract void compose(MessageReceivedEvent event, String... args);

    public abstract void compose(SlashCommandInteractionEvent event);

    public abstract boolean checkContext(Member member, TextChannel textChannel);

    public abstract <T> T execute(Object... args);

    public boolean match(String command) {
        if (command.equalsIgnoreCase(alias))
            return true;

        for (String s : aliases) {
            if (s.equalsIgnoreCase(command))
                return true;
        }

        return false;
    }

    public void handleException(MessageReceivedEvent event, RuntimeException exception) {
        Tama.INSTANCE.getCommandManager().handleException(event, exception);
        throw exception;
    }

    public void handleException(SlashCommandInteractionEvent event, RuntimeException exception) {
        Tama.INSTANCE.getCommandManager().handleException(event, exception);
        throw exception;
    }

    public boolean matchArgsLength(String text) {
        return matchArgsLength(text.split(" "));
    }

    public boolean matchArgsLength(String[] text) {
        return text.length >= argsLength;
    }

    public String getAlias() {
        return alias;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public CommandType getType() {
        return type;
    }

    public String[] getAliases() {
        return aliases;
    }

    public int getArgsLength() {
        return argsLength;
    }

    public List<OptionData> getOptions() {
        return options;
    }

    public static class OptionData {

        private final OptionType optionType;
        private final String name;
        private final String description;
        private final boolean required;
        private final boolean autoComplete;

        public OptionData(OptionType optionType, String name, String description, boolean required, boolean autoComplete) {
            this.optionType = optionType;
            this.name = name;
            this.description = description;
            this.required = required;
            this.autoComplete = autoComplete;
        }

        public OptionType getOptionType() {
            return optionType;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public boolean isRequired() {
            return required;
        }

        public boolean isAutoComplete() {
            return autoComplete;
        }
    }
}

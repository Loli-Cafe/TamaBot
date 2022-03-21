package uwu.narumi.tama.command.impl.admin;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandException;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.Objects;

@CommandInfo(
        alias = "commands",
        description = "Manages commands channels whitelist",
        usage = "/commands [channel]",
        type = CommandType.ADMIN
)
public class CommandsCommand extends Command {

    public CommandsCommand() {
        super(
                new OptionData(OptionType.CHANNEL, "channel", "kurwa no nei wiem", false, false)
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

            TextChannel textChannel;
            if (!event.getMessage().getMentionedChannels().isEmpty())
                textChannel = event.getMessage().getMentionedChannels().get(0);
            else
                textChannel = event.getTextChannel();

            if (guild.getWhitelistedCommandsChannels().contains(textChannel.getId())) {
                guild.getBlacklistedLevelingChannels().remove(textChannel.getId());
                event.getTextChannel().sendMessageEmbeds(EmbedHelper.success(String.format("Commands are now %s on %s", "enabled", textChannel.getAsMention()))).queue();
            } else {
                guild.getWhitelistedCommandsChannels().add(textChannel.getId());
                event.getTextChannel().sendMessageEmbeds(EmbedHelper.success(String.format("Commands are now %s on %s", "disabled", textChannel.getAsMention()))).queue();
            }
        });
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        checkContext(event.getMember(), event.getTextChannel());

        Tama.INSTANCE.getGuildManager().findGuild(Objects.requireNonNull(event.getGuild()).getId()).thenAccept(guild -> {
            if (guild == null)
                handleException(event, new CommandException("Can't fetch guild"));

            OptionMapping optionMapping = event.getOption("channel");
            TextChannel textChannel;
            if (optionMapping != null)
                textChannel = optionMapping.getAsTextChannel();
            else
                textChannel = event.getTextChannel();

            if (guild.getWhitelistedCommandsChannels().contains(textChannel.getId())) {
                guild.getBlacklistedLevelingChannels().remove(textChannel.getId());
                event.getTextChannel().sendMessageEmbeds(EmbedHelper.success(String.format("Commands are now %s on %s", "enabled", textChannel.getAsMention()))).queue();
            } else {
                guild.getWhitelistedCommandsChannels().add(textChannel.getId());
                event.getTextChannel().sendMessageEmbeds(EmbedHelper.success(String.format("Commands are now %s on %s", "disabled", textChannel.getAsMention()))).queue();
            }
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
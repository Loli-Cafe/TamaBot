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
        description = "Enables/Disabled commands blacklist | Manages commands channels whitelist",
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

            if (args.length > 0) {
                TextChannel textChannel;
                if (!event.getMessage().getMentionedChannels().isEmpty())
                    textChannel = event.getMessage().getMentionedChannels().get(0);
                else
                    textChannel = event.getTextChannel();

                if (guild.getWhitelistedCommandsChannels().contains(textChannel.getId())) {
                    guild.getWhitelistedCommandsChannels().remove(textChannel.getId());
                    event.getTextChannel().sendMessageEmbeds(EmbedHelper.success(String.format("Commands on %s are now %s", textChannel.getAsMention(), "enabled"))).queue();
                } else {
                    guild.getWhitelistedCommandsChannels().add(textChannel.getId());
                    event.getTextChannel().sendMessageEmbeds(EmbedHelper.success(String.format("Commands on %s are now %s", textChannel.getAsMention(), "enabled"))).queue();
                }
            } else {
                guild.setCommandsBlacklist(!guild.isCommandsBlacklist());
                event.getTextChannel().sendMessageEmbeds(EmbedHelper.success(String.format("Commands per channel was %s on this server", (guild.isCommandsBlacklist() ? "enabled" : "disabled")))).queue();
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
            if (optionMapping != null) {
                TextChannel textChannel = optionMapping.getAsTextChannel();
                if (guild.getWhitelistedCommandsChannels().contains(textChannel.getId())) {
                    guild.getWhitelistedCommandsChannels().remove(textChannel.getId());
                    event.replyEmbeds(EmbedHelper.success(String.format("Commands on %s are now %s", textChannel.getAsMention(), "enabled"))).queue();
                } else {
                    guild.getWhitelistedCommandsChannels().add(textChannel.getId());
                    event.replyEmbeds(EmbedHelper.success(String.format("Commands on %s are now %s", textChannel.getAsMention(), "enabled"))).queue();
                }
            } else {
                guild.setCommandsBlacklist(!guild.isCommandsBlacklist());
                event.replyEmbeds(EmbedHelper.success(String.format("Commands per channel was %s on this server", (guild.isCommandsBlacklist() ? "enabled" : "disabled")))).queue();
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
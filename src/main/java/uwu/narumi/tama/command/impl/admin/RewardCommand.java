package uwu.narumi.tama.command.impl.admin;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandException;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.Objects;

@CommandInfo(
        alias = "reward",
        description = "Sets reward for given level",
        usage = "/reward <level> <role>",
        type = CommandType.ADMIN,
        argsLength = 2
)
public class RewardCommand extends Command {

    public RewardCommand() {
        super(
                new OptionData(OptionType.INTEGER, "level", "Level", true, false),
                new OptionData(OptionType.ROLE, "role", "Role to be added", true, false)
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

            try {
                if (event.getMessage().getMentionedRoles().isEmpty())
                    return;

                int level = Integer.parseInt(args[0]);
                Role role = event.getMessage().getMentionedRoles().get(0);

                guild.getLevelRewards().put(level, Objects.requireNonNull(role).getId());
                event.getTextChannel().sendMessageEmbeds(
                        EmbedHelper.success(String.format("Role reward for level %s is now %s", level, role.getAsMention()))).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        checkContext(event.getMember(), event.getTextChannel());

        Tama.INSTANCE.getGuildManager().findGuild(Objects.requireNonNull(event.getGuild()).getId()).thenAccept(guild -> {
            if (guild == null)
                handleException(event, new CommandException("Can't fetch guild"));

            int level = Objects.requireNonNull(event.getOption("level")).getAsInt();
            Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();

            guild.getLevelRewards().put(level, role.getId());
            event.getTextChannel().sendMessageEmbeds(
                    EmbedHelper.success(String.format("Role reward for level %s is now %s", level, role.getAsMention()))).queue();
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
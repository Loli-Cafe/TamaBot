package uwu.narumi.tama.command.impl.user;

import net.dv8tion.jda.api.entities.Member;
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

@CommandInfo(
        alias = "level",
        description = "Your level",
        usage = "/level [user]",
        type = CommandType.USER,
        aliases = "rank"
)
public class LevelCommand extends Command {

    public LevelCommand() {
        super(
                new OptionData(OptionType.USER, "user", "User", false, false)
        );
    }

    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        User dcUser = event.getMessage().getMentionedUsers().isEmpty() ? event.getAuthor() : event.getMessage().getMentionedUsers().get(0);
        Tama.INSTANCE.getUserManager().findGuildUser(event.getGuild().getId(), dcUser.getId()).thenAccept(user -> {
            event.getTextChannel().sendMessageEmbeds(EmbedHelper.levelInfo(dcUser, user)).queue();
        });
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        OptionMapping optionMapping = event.getOption("user");
        User dcUser = optionMapping != null ? optionMapping.getAsUser() : event.getUser();
        Tama.INSTANCE.getUserManager().findGuildUser(event.getGuild().getId(), dcUser.getId()).thenAccept(user -> {
            event.replyEmbeds(EmbedHelper.levelInfo(dcUser, user)).queue();
        });
    }

    @Override
    public boolean checkContext(Member member, TextChannel textChannel) {
        return false;
    }

    @Override
    public <T> T execute(Object... args) {
        return null;
    }
}

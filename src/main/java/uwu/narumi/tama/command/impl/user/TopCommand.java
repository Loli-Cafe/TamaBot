package uwu.narumi.tama.command.impl.user;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.command.CommandInfo;
import uwu.narumi.tama.command.CommandType;
import uwu.narumi.tama.helper.discord.EmbedHelper;
import uwu.narumi.tama.user.impl.GuildUser;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@CommandInfo(
        alias = "top",
        description = "Top",
        usage = "/top",
        type = CommandType.USER
)
public class TopCommand extends Command {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(1);

    /*
    TODO: FIX
     */
    @Override
    public void compose(MessageReceivedEvent event, String... args) {
        forkJoinPool.execute(() -> {
            List<GuildUser> users = Tama.INSTANCE.getDataBase().fetchAll(event.getGuild().getId());
            users.sort(Comparator.comparingInt(GuildUser::getGlobalExperience).reversed());

            if (users.isEmpty()) {
                event.getChannel().sendMessageEmbeds(EmbedHelper.error("No active users")).queue();
                return;
            }

            event.getChannel().sendMessageEmbeds(EmbedHelper.levelTop(users, event.getAuthor(), 20)).queue();
        });
    }

    @Override
    public void compose(SlashCommandInteractionEvent event) {
        forkJoinPool.execute(() -> {
            List<GuildUser> users = Tama.INSTANCE.getDataBase().fetchAll(event.getGuild().getId());
            users.sort(Comparator.comparingInt(GuildUser::getGlobalExperience).reversed());

            if (users.isEmpty()) {
                event.replyEmbeds(EmbedHelper.error("No active users")).queue();
                return;
            }

            event.replyEmbeds(EmbedHelper.levelTop(users, event.getUser(), 20)).queue();
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

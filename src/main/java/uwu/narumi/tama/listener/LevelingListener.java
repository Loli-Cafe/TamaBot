package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.helper.discord.EmbedHelper;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LevelingListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot())
            return;

        Tama.INSTANCE.getGuildManager().findGuild(event.getGuild().getId()).thenAccept(guild -> {
            if (!guild.isLeveling())
                return;

            if (guild.getBlacklistedLevelingChannels().contains(event.getTextChannel().getId()))
                return;

            //TODO: Move to BotUserController
            Tama.INSTANCE.getUserManager().findGuildUser(event.getGuild().getId(), event.getAuthor().getId()).thenAccept(user -> {
                if (user == null)
                    return;

                if (user.getLastMessageTime() != -1 && (System.currentTimeMillis() - user.getLastMessageTime() < 1000)) {
                    user.setExperience(user.getExperience() - 5);
                    return;
                }

                if (System.currentTimeMillis() - user.getLastMessageTime() > TimeUnit.MINUTES.toMillis(10))
                    user.setActiveTime(-1);

                if (user.getMessages() > 2 && user.getActiveTime() == -1)
                    user.setActiveTime(System.currentTimeMillis());

                if (System.currentTimeMillis() - user.getActiveTime() > TimeUnit.MINUTES.toMinutes(1)) {
                    int exp = 0;

                    exp += Math.min((event.getMessage().getContentDisplay().length() % 5) - 1, 50);
                    exp += event.getMessage().getAttachments().isEmpty() ? 0 : 10;
                    exp += Math.min(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - user.getActiveTime()) % 10, 50);

                    user.setExperience(user.getExperience() + exp);
                    user.setGlobalExperience(user.getGlobalExperience() + exp);
                    if (user.getExperience() >= user.getExperienceBarrier()) {
                        user.setLevel(user.getLevel() + 1);
                        user.setExperienceBarrier(user.getLevel() * 100);
                        user.setExperience(0);

                        Role role = event.getGuild().getRoleById(guild.getLevelRewards().get(user.getLevel()));
                        TextChannel notificationChannel = event.getGuild().getTextChannelById(guild.getNotificationChannel());
                        if (notificationChannel == null)
                            return;

                        if (role == null) {
                            notificationChannel.sendMessageEmbeds(EmbedHelper.error("Role doesn't exists")).queue();
                            return;
                        }

                        event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), role).queue();
                        notificationChannel.sendMessageEmbeds(EmbedHelper.levelGained(event.getAuthor(), user.getLevel(), role)).queue();
                    }
                }

                user.setLastMessageTime(System.currentTimeMillis());
                user.setMessages(user.getMessages() + 1);
            });
        });
    }
}

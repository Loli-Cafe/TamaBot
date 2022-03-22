package uwu.narumi.tama.helper.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import uwu.narumi.features.misc.object.Image;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.command.Command;
import uwu.narumi.tama.user.impl.GuildUser;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class EmbedHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm yyyy.MM.dd");

    private static final Color PINK_COLOR = new Color(204, 0, 102);
    private static final Color RED_COLOR = new Color(162, 8, 8);
    private static final Color GREEN_COLOR = new Color(7, 162, 8);
    private static final Color BLUE_COLOR = new Color(0, 146, 204);

    //Move to some emote manager or something
    private static final String INFO = "<:info:952240262955016283>";
    private static final String NO = "<:no:950403273892982864>";
    private static final String YES = "<:yes:950403273712599080>";
    private static final String NANI = "<:nani:950403273645502496>";
    private static final String HI = "<:hi:950403273825857566>";
    private static final String TAMA = "<:tama:950402612166008933>";
    private static final String HORNI = "<a:horni:950402776213618698>";
    private static final String SEX = "<a:sex:950402664485777408>";
    private static final String PARTY = "\uD83E\uDD73";

    private EmbedHelper() {
    }

    public static MessageEmbed command(User user, Command command) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setDescription(String.format("Information about `%s` command", command.getAlias()))
                .setFooter(String.format("Invoked by: %s | %s", user.getName(), DATE_FORMAT.format(new Date())), user.getAvatarUrl());

        if (!command.getAlias().isEmpty())
            builder.addField("Main alias", String.format("```%s```", command.getAlias()), false);

        if (command.getAliases() != null && command.getAliases().length > 0)
            builder.addField("Aliases", String.format("```%s```", String.join(", ", command.getAliases())), false);

        if (!command.getUsage().isEmpty())
            builder.addField("Usage", String.format("```%s```", command.getUsage()), false);

        if (!command.getDescription().isEmpty())
            builder.addField("Description", String.format("```%s```", command.getDescription()), false);

        builder.addField("Type", String.format("```%s```", command.getType().name()), false);

        return builder.build();
    }

    public static MessageEmbed help(User user, List<Command> commands) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setDescription(String.format("Prefix: `%s`", Tama.INSTANCE.getCommandManager().getPrefix()))
                .setFooter(String.format("Invoked by: %s | %s", user.getName(), DATE_FORMAT.format(new Date())), user.getAvatarUrl());

        commands.forEach(command -> {
            StringBuilder commandInfo = new StringBuilder();
            if (command.getAliases() != null && command.getAliases().length > 0)
                commandInfo.append(String.format("*├*Aliases: `%s`", String.join(", ", command.getAliases()))).append("\n");

            if (!command.getUsage().isEmpty())
                commandInfo.append(String.format("*├*Usage: `%s`", command.getUsage())).append("\n");

            if (!command.getDescription().isEmpty())
                commandInfo.append(String.format("*├*Description: `%s`", command.getDescription())).append("\n");

            commandInfo.append(String.format("*└*Type: `%s`", command.getType().name()));

            builder.addField(command.getAlias().toUpperCase(Locale.ROOT), commandInfo.toString(), false);
        });
        return builder.build();
    }

    public static MessageEmbed image(User user, String tags, Image image) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setImage(image.getUrl())
                .setFooter(String.format("Invoked by: %s | %s", user.getName(), DATE_FORMAT.format(new Date())), user.getAvatarUrl());

        if (EmbedBuilder.URL_PATTERN.matcher(image.getSource()).matches())
            embedBuilder.setTitle("Source", image.getSource());

        if (!tags.isEmpty())
            embedBuilder.setDescription(String.format("Tags: `%s`", tags));

        return embedBuilder.build();
    }

    public static MessageEmbed invite(User user, String link) {
        return new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setTitle(String.format("%s **TamaBot Invite**", TAMA))
                .setDescription(String.format("*%s*", link))
                .setFooter(String.format("Invoked by: %s | %s", user.getName(), DATE_FORMAT.format(new Date())), user.getAvatarUrl())
                .build();
    }

    public static MessageEmbed action(String message, Image image) {
        return action(message, image.getUrl());
    }

    public static MessageEmbed action(String message, String url) {
        return new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setDescription(String.format("**%s**", message))
                .setImage(url)
                .build();
    }

    public static MessageEmbed error(String message) {
        return new EmbedBuilder()
                .setColor(RED_COLOR)
                .setDescription(String.format("%s %s", NO, message))
                .build();
    }

    public static MessageEmbed success(String message) {
        return new EmbedBuilder()
                .setColor(GREEN_COLOR)
                .setDescription(String.format("%s %s", YES, message))
                .build();
    }

    public static MessageEmbed info(String topic, String info) {
        return new EmbedBuilder()
                .setColor(BLUE_COLOR)
                .setAuthor(topic, "https://github.com/Loli-Cafe/TamaBot", "https://i.imgur.com/WXpPiD5.png")
                .setDescription(info)
                .build();
    }

    public static MessageEmbed levelGained(User user, int level, Role role) {
        return new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setAuthor("Congratulations!" + PARTY, user.getAvatarUrl(), user.getAvatarUrl())
                .setDescription(String.format("%s, you've reached level %s and got %s role!", user.getAsMention(), level, role.getAsMention()))
                .build();
    }

    public static MessageEmbed levelInfo(User user, GuildUser guildUser) {
        return new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setThumbnail(user.getAvatarUrl())
                .setDescription(user.getAsMention())
                .addField("Level", String.format("*└*`%s`", guildUser.getLevel()), true)
                .addField("Experience", String.format("*└*`%s`", guildUser.getGlobalExperience()), true)
                .addField("Next Level", String.format("*└*`%s/%s`", guildUser.getExperience(), guildUser.getExperienceBarrier()), true)
                //.setAuthor("Congratulations!" + PARTY, user.getAvatarUrl(), user.getAvatarUrl())
                //.setDescription(String.format("%s, you've reached level %s and got %s role!", user.getAsMention(), level, role.getAsMention()))
                .build();
    }

    /*
        TODO: Recode
     */
    public static MessageEmbed levelTop(List<GuildUser> users, User user, int max) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Guild Score Leaderboards!")
                .setTitle("Active Score")
                .setColor(PINK_COLOR);

        boolean added = false;
        embedBuilder.setDescription("\n");
        for (int i = 0; i < users.size(); i++) {
            GuildUser guildUser = users.get(i);
            boolean isInvoker = guildUser.getId().equals(user.getId());

            if (i < Math.min(users.size(), max)) {
                if (!added)
                    added = isInvoker;

                String base = isInvoker ? "**#%s |** %s **XP:** `%s`\n" : "#%s | %s XP: `%s`\n";
                embedBuilder.appendDescription(String.format(base, i + 1, guildUser.getMention(), guildUser.getGlobalExperience()));
            } else if (added)
                break;

            if (!added && isInvoker) {
                embedBuilder.appendDescription(String.format("**#%s |** %s **XP:** `%s`\n", i + 1, user.getAsMention(), guildUser.getGlobalExperience()));
                break;
            }
        }

        return embedBuilder.build();
    }

    public static SimpleDateFormat getDateFormat() {
        return DATE_FORMAT;
    }

    public static Color getPinkColor() {
        return PINK_COLOR;
    }

    public static Color getRedColor() {
        return RED_COLOR;
    }

    public static Color getGreenColor() {
        return GREEN_COLOR;
    }

    public static String getNo() {
        return NO;
    }

    public static String getYes() {
        return YES;
    }

    public static String getNani() {
        return NANI;
    }

    public static String getHi() {
        return HI;
    }

    public static String getTama() {
        return TAMA;
    }

    public static String getHorni() {
        return HORNI;
    }

    public static String getSex() {
        return SEX;
    }

    public static String getParty() {
        return PARTY;
    }
}

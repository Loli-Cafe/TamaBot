package uwu.narumi.tama.helper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import uwu.narumi.anime.object.Image;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.command.Command;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class EmbedHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm yyyy.MM.dd");

    private static final Color PINK_COLOR = new Color(204, 0, 102);
    private static final Color RED_COLOR = new Color(204, 0, 0);
    private static final Color GREEN_COLOR = new Color(0, 204, 0);

    //Move to some emote manager or something
    private static final String NO = "<:no:950403273892982864>";
    private static final String YES = "<:yes:950403273712599080>";
    private static final String NANI = "<:nani:950403273645502496>";
    private static final String HI = "<:hi:950403273825857566>";
    private static final String TAMA = "<:tama:950402612166008933>";
    private static final String HORNI = "<a:horni:950402776213618698>";
    private static final String SEX = "<a:sex:950402664485777408>";

    private EmbedHelper() {
    }

    public static MessageEmbed command(User user, Command command) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setDescription(String.format("Information about `%s` command", command.getAlias()))
                .setFooter(String.format("Invoked by: %s | %s", user.getName(), DATE_FORMAT.format(new Date())), user.getAvatarUrl());

        if (!command.getAlias().isEmpty())
            builder.addField("Main alias", String.format("```%s```", command.getAlias()), false);

        if (command.getAliases() != null && command.getAlias().length() > 0)
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
            if (command.getAliases() != null && command.getAlias().length() > 0)
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

    public static MessageEmbed invite(User user) {
        return new EmbedBuilder()
                .setColor(PINK_COLOR)
                .setTitle(String.format("%s **TamaBot Invite**", TAMA))
                .setDescription(String.format("*%s*", "https://i-want-to-fuck-anime-girls.club/u/P7OKk90d"))
                .setFooter(String.format("Invoked by: %s | %s", user.getName(), DATE_FORMAT.format(new Date())), user.getAvatarUrl())
                .build();
    }

    public static MessageEmbed error(String message) {
        return new EmbedBuilder()
                .setColor(RED_COLOR)
                .setDescription(String.format("%s `%s`", NO, message))
                .build();
    }

    public static MessageEmbed success(String message) {
        return new EmbedBuilder()
                .setColor(GREEN_COLOR)
                .setDescription(String.format("%s `%s`", YES, message))
                .build();
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
}

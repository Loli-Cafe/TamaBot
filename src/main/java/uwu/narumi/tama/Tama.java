package uwu.narumi.tama;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.internal.JDAImpl;
import uwu.narumi.tama.command.CommandManager;
import uwu.narumi.tama.command.impl.anime.GelbooruCommand;
import uwu.narumi.tama.command.impl.anime.KonachanCommand;
import uwu.narumi.tama.command.impl.anime.SafebooruCommand;
import uwu.narumi.tama.command.impl.anime.YandereCommand;
import uwu.narumi.tama.command.impl.user.HelpCommand;
import uwu.narumi.tama.command.impl.user.InviteCommand;
import uwu.narumi.tama.listener.ChatListener;
import uwu.narumi.tama.listener.CommandListener;
import uwu.narumi.tama.listener.PingListener;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public enum Tama {

    INSTANCE;

    Properties properties;
    JDA jda;
    CommandManager commandManager;

    public void load(Path configPath) {
        try {
            properties = new Properties();
            properties.load(Files.newInputStream(configPath));

            jda = JDABuilder.createDefault(properties.getProperty("discord.bot.token")).build();
            commandManager = new CommandManager(
                    //new DanbooruCommand(), some issues idk
                    new GelbooruCommand(),
                    new KonachanCommand(),
                    new SafebooruCommand(),
                    new YandereCommand(),

                    new HelpCommand(),
                    new InviteCommand()
            );

            //You know what jda? fuck you
            commandManager.getCommands().forEach(command -> {
                CommandCreateAction action = jda.upsertCommand(command.getAlias(), command.getDescription().isEmpty() ? "?" : command.getDescription());
                if (command.getOptions() != null && !command.getOptions().isEmpty()) {
                    command.getOptions().forEach(optionData -> action.addOption(optionData.getOptionType(), optionData.getName(), optionData.getDescription(), optionData.isRequired(), optionData.isAutoComplete()).queue());
                }

                action.queue();
            });

            jda.addEventListener(
                    new ChatListener(),
                    new CommandListener(),
                    new PingListener()
            );

            setPresence(
                    Activity.ActivityType.valueOf(properties.getProperty("discord.bot.activity.type")),
                    properties.getProperty("discord.bot.activity.message"),
                    properties.getProperty("discord.bot.activity.url")
            );
        } catch (Exception e) {
            JDAImpl.LOG.error("Couldn't initialize bot", e);
            System.exit(69);
        }
    }

    private void setPresence(Activity.ActivityType activityType, String message, String url) {
        if (activityType == Activity.ActivityType.STREAMING && (url == null || url.isEmpty()))
            throw new IllegalArgumentException();

        if (activityType == Activity.ActivityType.STREAMING) {
            jda.getPresence().setPresence(Activity.of(activityType, message, url), true);
        } else {
            jda.getPresence().setPresence(Activity.of(activityType, message), true);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public JDA getJda() {
        return jda;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}

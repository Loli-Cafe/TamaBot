package uwu.narumi.tama;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.internal.JDAImpl;
import uwu.narumi.tama.command.CommandManager;
import uwu.narumi.tama.command.impl.admin.*;
import uwu.narumi.tama.command.impl.anime.*;
import uwu.narumi.tama.command.impl.nsfw.*;
import uwu.narumi.tama.command.impl.nsfw.rp.*;
import uwu.narumi.tama.command.impl.user.HelpCommand;
import uwu.narumi.tama.command.impl.user.InviteCommand;
import uwu.narumi.tama.command.impl.user.LevelCommand;
import uwu.narumi.tama.command.impl.user.TopCommand;
import uwu.narumi.tama.database.DataBase;
import uwu.narumi.tama.database.DataBaseFactory;
import uwu.narumi.tama.guild.BotGuildManager;
import uwu.narumi.tama.guild.controller.BotGuildController;
import uwu.narumi.tama.listener.*;
import uwu.narumi.tama.user.BotUserManager;
import uwu.narumi.tama.user.controller.BotUserController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public enum Tama {

    INSTANCE;

    Properties properties;
    Properties databaseProperties;

    DataBase dataBase;

    JDA jda;
    CommandManager commandManager;

    BotGuildManager guildManager;
    BotGuildController guildController;
    ReentrantLock guildLock;

    BotUserManager userManager;
    BotUserController userController;
    ReentrantLock guildUserLock;
    ReentrantLock sharedUserLock;

    public void load(Path configPath) {
        try {
            properties = new Properties();
            properties.load(Files.newInputStream(configPath));

            databaseProperties = new Properties();
            databaseProperties.load(Files.newInputStream(Paths.get(properties.getProperty("bot.database.properties.path"))));

            dataBase = DataBaseFactory.createInstance(properties.getProperty("bot.database.type"))
                    .orElseThrow(() -> new RuntimeException(String.format("Database %s not found", properties.getProperty("bot.database.type"))));
            dataBase.connect();

            guildManager = new BotGuildManager();
            guildController = new BotGuildController();
            guildLock = new ReentrantLock();

            userManager = new BotUserManager();
            userController = new BotUserController();
            guildUserLock = new ReentrantLock();
            sharedUserLock = new ReentrantLock();

            jda = JDABuilder.createDefault(properties.getProperty("discord.bot.token")).build();
            commandManager = new CommandManager(
                    new AmputeeCommand(),
                    new AnalCommand(),
                    new BeatCommand(),
                    new BestialityCommand(),
                    new BlowJobCommand(),
                    new BrainFuckCommand(),
                    new BrandCommand(),
                    new CactusCommand(),
                    new CreampieCommand(),
                    new CuntbustCommand(),
                    new DeepthroatCommand(),
                    new DestroyCommand(),
                    new DobulePenetrationCommand(),
                    new DrugCommand(),
                    new EarFuckCommand(),
                    new ElectroTortureCommand(),
                    new EyeFuckCommand(),
                    new FistAnalCommand(),
                    new FistPussyCommand(),
                    new FootJobCommand(),
                    new GapeAnalCommand(),
                    new GapePussyCommand(),
                    new GropeAssCommand(),
                    new GropeBoobsCommand(),
                    new HandJobCommand(),
                    new HumiliateCommand(),
                    new LickNipplesCommand(),
                    new LickPussyCommand(),
                    new MasturbateCommand(),
                    new MindBreakCommand(),
                    new MindControlCommand(),
                    new PetPlayCommand(),
                    new PierceClitCommand(),
                    new PierceLabiaCommand(),
                    new PierceNipplesCommand(),
                    new PissCommand(),
                    new ProlapseAnusCommand(),
                    new ProlapsePussyCommand(),
                    new ProstituteCommand(),
                    new RapeCommand(),
                    new RimJobCommand(),
                    new ScatCommand(),
                    new SexCommand(),
                    new SlapCommand(),
                    new SlaveCommand(),
                    new SpankCommand(),
                    new TitJobCommand(),
                    new TortureCommand(),
                    new TouchPussyCommand(),
                    new WhipCommand(),

                    new BakaCommand(),
                    new CuddleCommand(),
                    new FeedCommand(),
                    new HugCommand(),
                    new KissCommand(),
                    new PatCommand(),
                    new PokeCommand(),
                    new SmugCommand(),
                    new StepCommand(),
                    new TickleCommand(),

                    new DanbooruCommand(),
                    new GelbooruCommand(),
                    new KonachanCommand(),
                    new LolibooruCommand(),
                    new NekosLifeCommand(),
                    new SafebooruCommand(),
                    new YandereCommand(),

                    new HelpCommand(),
                    new InviteCommand(),
                    new LevelCommand(),
                    new TopCommand(),

                    new CommandsCommand(),
                    //new LanguageCommand(),
                    new LevelingCommand(),
                    new NotificationsCommand(),
                    new PrefixCommand(),
                    new ProxyCommand(),
                    new RewardCommand()
            );

            if (dataBase.isConnected()) { //TODO
                commandManager.registerCommands();
            }

            //You know what jda? fuck you
            commandManager.getCommands().forEach(command -> {
                CommandCreateAction action = jda.upsertCommand(command.getAlias(), command.getDescription().isEmpty() ? "?" : command.getDescription());
                if (command.getOptions() != null && !command.getOptions().isEmpty()) {
                    command.getOptions().forEach(optionData -> action.addOption(optionData.getOptionType(), optionData.getName(), optionData.getDescription(), optionData.isRequired(), optionData.isAutoComplete()).queue());
                }

                action.queue();
            });

            jda.addEventListener(
                    new DatabaseListener(),
                    new CommandListener(),
                    new ChatListener(),
                    new PingListener(),
                    new LevelingListener(),
                    new ProxyListener()
            );

            setPresence(
                    Activity.ActivityType.valueOf(properties.getProperty("discord.bot.activity.type")),
                    properties.getProperty("discord.bot.activity.message"),
                    properties.getProperty("discord.bot.activity.url")
            );

            Runtime.getRuntime().addShutdownHook(new Thread(() -> dataBase.saveAll()));
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

    public Properties getDatabaseProperties() {
        return databaseProperties;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public JDA getJda() {
        return jda;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public BotGuildManager getGuildManager() {
        return guildManager;
    }

    public BotGuildController getGuildController() {
        return guildController;
    }

    public ReentrantLock getGuildLock() {
        return guildLock;
    }

    public BotUserManager getUserManager() {
        return userManager;
    }

    public BotUserController getUserController() {
        return userController;
    }

    public ReentrantLock getGuildUserLock() {
        return guildUserLock;
    }

    public ReentrantLock getSharedUserLock() {
        return sharedUserLock;
    }
}

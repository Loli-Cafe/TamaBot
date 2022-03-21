package uwu.narumi.tama.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uwu.narumi.tama.Tama;
import uwu.narumi.tama.helper.network.MagicEncoder;

/*
    Fuck discord for banning (hentai) anime loli
    Fuck antis
    Fuck people how are offended by fucking art go help real fucking childerns lol
    How the fuck children exploitation works when it comes to art?
    The guys putting fucking dick into display and they can rape art?
 */
public class ProxyListener extends ListenerAdapter {

    private static final String SECRET_FORMAT = "%s||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||%s";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot() || !hasImage(event.getMessage()))
            return;

        Guild proxyGuild = Tama.INSTANCE.getJda().getGuildById(Tama.INSTANCE.getProperties().getProperty("proxy.guild.id"));
        if (proxyGuild == null)
            return;

        TextChannel channelProxy = proxyGuild.getTextChannelById(Tama.INSTANCE.getProperties().getProperty("proxy.guild.channel.id"));
        if (channelProxy == null)
            return;

        Tama.INSTANCE.getGuildManager().findGuild(event.getGuild().getId()).thenAccept(guild -> {
            if (!guild.isProxy())
                return;

            if (guild.getWhitelistedProxyChannels().isEmpty() || guild.getWhitelistedProxyChannels().contains(event.getTextChannel().getId())) {
                event.getMessage().getAttachments().stream().filter(Message.Attachment::isImage).forEach(attachment -> attachment.retrieveInputStream().thenAccept(inputStream -> {
                    Message message = channelProxy.sendFile(inputStream, attachment.getFileName()).complete();
                    event.getChannel().sendMessage(convert(" ", message.getAttachments().get(0).getUrl())).complete();
                }));

                event.getMessage().delete().complete();
            }
        });
    }

    private boolean hasImage(Message message) {
        for (Message.Attachment attachment : message.getAttachments()) {
            if (attachment.isImage())
                return true;
        }

        return false;
    }

    private String convert(String prefix, String url) {
        return String.format(SECRET_FORMAT, prefix, " https://" + MagicEncoder.encode(url.replace("https://", "")));
    }
}

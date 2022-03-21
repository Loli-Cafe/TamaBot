import net.dv8tion.jda.internal.JDAImpl;
import uwu.narumi.tama.Tama;

import java.nio.file.Path;

public class Bootstrap {

    public static void main(String[] args) {
        //I18n.load("messages", "messages");
        Tama.INSTANCE.load(Path.of("configuration.properties"));
        JDAImpl.LOG.info("{} ~ {}", Tama.INSTANCE.getJda().getSelfUser().getName(), Tama.INSTANCE.getJda().getSelfUser().getApplicationId());
    }
}

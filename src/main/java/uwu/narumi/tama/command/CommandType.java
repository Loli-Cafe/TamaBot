package uwu.narumi.tama.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public enum CommandType {

    ANIME, USER, MODERATOR, ADMIN; //in future

    public boolean canInvoke(Member member) {
        switch (this) {
            case MODERATOR:
                return member.hasPermission(Permission.MESSAGE_MANAGE);
            case ADMIN:
                return member.hasPermission(Permission.ADMINISTRATOR);
            // case ANIME:
            //     return member.getRoles().contains();
            default:
                return true;
        }
    }
}

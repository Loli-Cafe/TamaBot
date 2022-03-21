package uwu.narumi.tama.user.identifier;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public final class Identifier {

    private final String guildId;
    private final String userId;

    private Identifier(String guildId, String userId) {
        this.guildId = guildId;
        this.userId = userId;
    }

    public static Identifier of(String guildId, String userId) {
        return new Identifier(guildId, userId);
    }

    public static Identifier of(Guild guild, String userId) {
        return new Identifier(guild.getId(), userId);
    }

    public static Identifier of(String guildId, User user) {
        return new Identifier(guildId, user.getId());
    }

    public static Identifier of(Guild guild, User user) {
        return new Identifier(guild.getId(), user.getId());
    }

    public static Identifier of(String guildId, Member member) {
        return new Identifier(guildId, member.getUser().getId());
    }

    public static Identifier of(Guild guild, Member member) {
        return new Identifier(guild.getId(), member.getUser().getId());
    }

    public String getGuildId() {
        return guildId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        if (!Objects.equals(guildId, that.guildId)) return false;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        int result = guildId != null ? guildId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "guildId='" + guildId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

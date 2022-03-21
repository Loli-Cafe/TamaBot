package uwu.narumi.tama.user.impl;

import uwu.narumi.tama.user.IUser;

public class GuildUser implements IUser {

    private String id;

    private int level;
    private int globalExperience;
    private int experience;
    private int experienceBarrier = 0;
    private int messages;

    private long lastMessageTime = -1;
    private long activeTime = -1;

    public GuildUser() {
    }

    public GuildUser(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGlobalExperience() {
        return globalExperience;
    }

    public void setGlobalExperience(int globalExperience) {
        this.globalExperience = globalExperience;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperienceBarrier() {
        return experienceBarrier;
    }

    public void setExperienceBarrier(int experienceBarrier) {
        this.experienceBarrier = experienceBarrier;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }
}

package uwu.narumi.tama.user.impl;

import uwu.narumi.features.reader.Reader;
import uwu.narumi.tama.user.IUser;

import java.util.ArrayList;
import java.util.List;

public class SharedUser implements IUser {

    private final List<Reader> readers = new ArrayList<>();
    private String id;
    private int globalLevel;
    private String language;

    public SharedUser() {
    }

    public SharedUser(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public int getGlobalLevel() {
        return globalLevel;
    }

    public String getLanguage() {
        return language;
    }

    public List<Reader> getReaders() {
        return readers;
    }
}

package com.dmytrobilokha.treen.notes;

import java.util.List;

public class Notebook {

    private final String username;
    private final List<String> notes;

    Notebook(String username, List<String> notes) {
        this.username = username;
        this.notes = notes;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getNotes() {
        return notes;
    }

}

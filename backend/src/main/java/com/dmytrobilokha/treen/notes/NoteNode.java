package com.dmytrobilokha.treen.notes;

import java.util.List;

public class NoteNode {

    private final String username;
    private final List<String> notes;

    NoteNode(String username, List<String> notes) {
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

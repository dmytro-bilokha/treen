package com.dmytrobilokha.treen.notes.rest;

import com.dmytrobilokha.treen.infra.exception.InvalidInputException;

import javax.annotation.CheckForNull;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NoteRequestValidator {

    private static final int MAX_TITLE_LENGTH = 150;
    private static final int MAX_LINK_LENGTH = 256;
    private static final int MAX_DESCRIPTION_LENGTH = 2000;

    void validateCreation(CreateNoteRequest request) throws InvalidInputException {
        validateTitleLink(request.getTitle(), request.getLink(), request.getDescription());
        validateNotZero(request.getParentId(), request.getVersion());
    }

    private void validateTitleLink(@CheckForNull String title,
                                   @CheckForNull String link,
                                   @CheckForNull String description) throws InvalidInputException {
        boolean titleIsEmpty = false;
        if (title == null || title.isBlank()) {
            titleIsEmpty = true;
        } else if (title.length() > MAX_TITLE_LENGTH) {
            throw new InvalidInputException("Title length should not exceed " + MAX_TITLE_LENGTH + " characters");
        }
        if (link == null || link.isBlank()) {
            if (titleIsEmpty) {
                throw new InvalidInputException("Either title or link should be provided");
            }
        } else if (link.length() > MAX_LINK_LENGTH) {
            throw new InvalidInputException("Link length should not exceed " + MAX_LINK_LENGTH + " characters");
        }
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new InvalidInputException("Description length should not exceed "
                    + MAX_DESCRIPTION_LENGTH + " characters");
        }
    }

    private void validateNotZero(Long... values) throws InvalidInputException {
        for (Long value : values) {
            if (value != null && value == 0) {
                throw new InvalidInputException("Values of ids and version should not be zero");
            }
        }
    }

    void validateUpdate(UpdateNoteRequest request) throws InvalidInputException {
        validateTitleLink(request.getTitle(), request.getLink(), request.getDescription());
        validateNotZero(request.getId(), request.getParentId(), request.getVersion());
    }

    void validateDelete(DeleteNoteRequest request) throws InvalidInputException {
        validateNotZero(request.getId(), request.getVersion());
    }

}

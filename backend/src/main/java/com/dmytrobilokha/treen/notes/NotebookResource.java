package com.dmytrobilokha.treen.notes;

import com.dmytrobilokha.treen.InternalApplicationException;
import com.dmytrobilokha.treen.OptimisticLockException;
import com.dmytrobilokha.treen.login.UserSessionData;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("/api/notebook")
public class NotebookResource {

    private NotebookService notebookService;
    private UserSessionData userSessionData;

    public NotebookResource() {
        //CDI
    }

    @Inject
    public NotebookResource(NotebookService notebookService, UserSessionData userSessionData) {
        this.notebookService = notebookService;
        this.userSessionData = userSessionData;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NoteNodeDto> getNotes() throws InternalApplicationException {
        var loginData = userSessionData.getLoginData();
        if (loginData == null) {
            throw new IllegalStateException("Authorization filter should not allow non authenticated access");
        }
        return notebookService.fetchUserNotebook(loginData.getUserId());
    }

    @PUT
    @Path("note")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<NoteNodeDto> updateNote(
            UpdateNoteRequest request) throws InternalApplicationException, OptimisticLockException {
        var loginData = userSessionData.getLoginData();
        if (loginData == null) {
            throw new IllegalStateException("Authorization filter should not allow non authenticated access");
        }
        var noteNode = new NoteNodeEntity(
                request.getId(),
                //TODO: replace with parent Id after adding it to the DTO
                null,
                loginData.getUserId(),
                request.getTitle(),
                request.getLink(),
                request.getDescription(),
                request.getVersion()
        );
        notebookService.updateNoteNode(noteNode);
        //TODO: change frontend to edit notes tree without getting it from the server
        return notebookService.fetchUserNotebook(loginData.getUserId());
    }

}

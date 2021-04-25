package com.dmytrobilokha.treen.notes.rest;

import com.dmytrobilokha.treen.InternalApplicationException;
import com.dmytrobilokha.treen.OptimisticLockException;
import com.dmytrobilokha.treen.login.rest.UserSessionData;
import com.dmytrobilokha.treen.notes.persistence.NewNote;
import com.dmytrobilokha.treen.notes.persistence.Note;
import com.dmytrobilokha.treen.notes.service.NotebookService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public NotebookDto getNotebook() throws InternalApplicationException {
        return notebookService.fetchUserNotebook(userSessionData.getAuthenticatedUserId());
    }

    //TODO: validate on request coming from the front-end

    @PUT
    @Path("note")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateNote(
            UpdateNoteRequest request) throws InternalApplicationException, OptimisticLockException {
        var noteNode = new Note(
                request.getId(),
                request.getParentId(),
                userSessionData.getAuthenticatedUserId(),
                request.getTitle(),
                request.getLink(),
                request.getDescription(),
                request.getVersion()
        );
        notebookService.updateNote(noteNode);
        return Response.ok().build();
    }

    @POST
    @Path("note")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public NoteDto createNote(
            CreateNoteRequest request) throws InternalApplicationException, OptimisticLockException {
        var noteNode = new NewNote(
                request.getParentId(),
                userSessionData.getAuthenticatedUserId(),
                request.getTitle(),
                request.getLink(),
                request.getDescription(),
                request.getVersion()
        );
        return notebookService.createNoteNode(noteNode);
    }

    @DELETE
    @Path("note")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteNotes(
            DeleteNoteRequest request) throws InternalApplicationException, OptimisticLockException {
        notebookService.removeNoteWithChildren(
                request.getId(), userSessionData.getAuthenticatedUserId(), request.getVersion());
        return Response.ok().build();
    }

}
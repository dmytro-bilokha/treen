package com.dmytrobilokha.treen.notes.rest;

import com.dmytrobilokha.treen.infra.exception.InternalApplicationException;
import com.dmytrobilokha.treen.infra.exception.InvalidInputException;
import com.dmytrobilokha.treen.login.rest.UserSessionData;
import com.dmytrobilokha.treen.notes.service.NotebookService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/api/notebook")
public class NotebookResource {

    private NotebookService notebookService;
    private NoteRequestConvertor requestConvertor;
    private UserSessionData userSessionData;

    public NotebookResource() {
        //CDI
    }

    @Inject
    public NotebookResource(
            NotebookService notebookService,
            NoteRequestConvertor requestConvertor,
            UserSessionData userSessionData) {
        this.notebookService = notebookService;
        this.requestConvertor = requestConvertor;
        this.userSessionData = userSessionData;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public NotebookDto getNotebook() throws InternalApplicationException {
        return notebookService.fetchUserNotebook(userSessionData.getAuthenticatedUserId());
    }

    @PUT
    @Path("note/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public NoteDto updateNote(
            @PathParam("id") @NotNull(message = "Note id must be provided") Long id,
            @Valid ChangeNoteRequest request) throws InternalApplicationException, InvalidInputException {
        var note = requestConvertor.convertUpdateRequest(id, request);
        return notebookService.updateNote(note);
    }

    @POST
    @Path("note")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public NoteDto createNote(
            @Valid ChangeNoteRequest request) throws InternalApplicationException, InvalidInputException {
        var note = requestConvertor.convertCreateRequest(request);
        return notebookService.createNoteNode(note);
    }

    @DELETE
    @Path("note/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteNote(
            @PathParam("id") @NotNull(message = "Note id must be provided") Long id,
            @Valid DeleteNoteRequest request
    ) throws InternalApplicationException, InvalidInputException {
        notebookService.removeNoteWithChildren(
                id, userSessionData.getAuthenticatedUserId(), request.getVersion());
        return Response.ok().build();
    }

    @PATCH
    @Path("note/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response moveNote(
            @PathParam("id") @NotNull(message = "Note id must be provided") Long id,
            @Valid MoveNoteRequest request) throws InternalApplicationException, InvalidInputException {
        notebookService.moveNoteWithChildren(
                id,
                request.getParentId(),
                userSessionData.getAuthenticatedUserId(),
                request.getVersion()
        );
        return Response.ok().build();
    }
}

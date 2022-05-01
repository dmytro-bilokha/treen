package com.dmytrobilokha.treen.notes.rest;

import com.dmytrobilokha.treen.infra.exception.InternalApplicationException;
import com.dmytrobilokha.treen.infra.exception.InvalidInputException;
import com.dmytrobilokha.treen.login.rest.UserSessionData;
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
    private NoteRequestValidator requestValidator;
    private NoteRequestConvertor requestConvertor;
    private UserSessionData userSessionData;

    public NotebookResource() {
        //CDI
    }

    @Inject
    public NotebookResource(
            NotebookService notebookService,
            NoteRequestValidator requestValidator,
            NoteRequestConvertor requestConvertor,
            UserSessionData userSessionData) {
        this.notebookService = notebookService;
        this.requestValidator = requestValidator;
        this.requestConvertor = requestConvertor;
        this.userSessionData = userSessionData;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public NotebookDto getNotebook() throws InternalApplicationException {
        return notebookService.fetchUserNotebook(userSessionData.getAuthenticatedUserId());
    }

    @PUT
    @Path("note")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public NoteDto updateNote(UpdateNoteRequest request) throws InternalApplicationException, InvalidInputException {
        requestValidator.validateUpdate(request);
        var note = requestConvertor.convertUpdateRequest(request);
        return notebookService.updateNote(note);
    }

    @POST
    @Path("note")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public NoteDto createNote(CreateNoteRequest request) throws InternalApplicationException, InvalidInputException {
        requestValidator.validateCreation(request);
        var note = requestConvertor.convertCreateRequest(request);
        return notebookService.createNoteNode(note);
    }

    @DELETE
    @Path("note")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteNotes(DeleteNoteRequest request) throws InternalApplicationException, InvalidInputException {
        requestValidator.validateDelete(request);
        notebookService.removeNoteWithChildren(
                request.getId(), userSessionData.getAuthenticatedUserId(), request.getVersion());
        return Response.ok().build();
    }

}

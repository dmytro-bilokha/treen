package com.dmytrobilokha.treen.notes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/api/notebook")
public class TreenResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Notebook getNotes(@Context SecurityContext securityContext) {
        var notebook = new Notebook(securityContext.getUserPrincipal().getName(),
                                    List.of("one", "two", "three"));
        return notebook;
    }

}

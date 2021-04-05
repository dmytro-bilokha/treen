package com.dmytrobilokha.treen.login;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/api/user")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@Context SecurityContext securityContext) {
        return new User(securityContext.getUserPrincipal().getName());
    }

}

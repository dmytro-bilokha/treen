package com.dmytrobilokha.treen.login;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/api/user")
public class UserResource {

    private UserData userData;

    public UserResource() {
        //Framework
    }

    @Inject
    public UserResource(UserData userData) {
        this.userData = userData;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserData getUserData(@Context HttpServletRequest request) {
        return userData;
    }

}

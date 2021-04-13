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

    private UserSessionData userSessionData;

    public UserResource() {
        //Framework
    }

    @Inject
    public UserResource(UserSessionData userSessionData) {
        this.userSessionData = userSessionData;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserDataResponse getUserData(@Context HttpServletRequest request) {
        var loginData = userSessionData.getLoginData();
        if (loginData == null) {
            throw new IllegalStateException(
                    "Authorization filter should not allow non-authenticated user to call this endpoint");
        }
        return new UserDataResponse(loginData.getLogin());
    }

}

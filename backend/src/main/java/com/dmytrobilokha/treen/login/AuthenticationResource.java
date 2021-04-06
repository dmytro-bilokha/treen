package com.dmytrobilokha.treen.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthenticationResource {

    //TODO: should I use logback? Or something else? How will it work with tomee logger?
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationResource.class);
    private static final int HALF_HOUR_SECONDS = 3600 / 2;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, LoginData loginData) {
        try {
            request.login(loginData.getLogin(), loginData.getPassword());
        } catch (ServletException e) {
            LOG.warn("Failed to login using {}", loginData, e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        //TODO: add support for indefinite session in case of "Remember me"
        var session = request.getSession(true);
        session.setMaxInactiveInterval(HALF_HOUR_SECONDS);
        return Response.ok().build();
    }

    @GET
    @Path("logout")
    public void logout(@Context HttpServletRequest request) {
        //TODO: do logout here
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}

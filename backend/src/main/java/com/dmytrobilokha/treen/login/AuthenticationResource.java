package com.dmytrobilokha.treen.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/auth")
public class AuthenticationResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationResource.class);
    private static final int HALF_HOUR_SECONDS = 3600 / 2;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, LoginData loginData) {
        if (loginData.getLogin() == null || loginData.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (loginData.getLogin().startsWith("tom") && loginData.getPassword().startsWith("tom")) {
            invalidateRequestSession(request);
            var session = request.getSession(true);
            session.setMaxInactiveInterval(loginData.isRememberMe() ? -1 : HALF_HOUR_SECONDS);
            session.setAttribute("userData", new UserData(loginData.getLogin()));
            return Response.ok().build();
        }
        LOG.warn("Failed to login using {}", loginData);
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("logout")
    public Response logout(@Context HttpServletRequest request) {
        invalidateRequestSession(request);
        return Response.ok().build();
    }

    private void invalidateRequestSession(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}

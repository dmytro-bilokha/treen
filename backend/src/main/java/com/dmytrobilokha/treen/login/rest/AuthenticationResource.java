package com.dmytrobilokha.treen.login.rest;

import com.dmytrobilokha.treen.infra.exception.InternalApplicationException;
import com.dmytrobilokha.treen.infra.exception.InvalidInputException;
import com.dmytrobilokha.treen.login.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
    private static final int MAX_LOG_PASSWD_LENGTH = 120;

    private UserSessionData userSessionData;
    private AuthenticationService authenticationService;

    public AuthenticationResource() {
        //Framework requirement
    }

    @Inject
    public AuthenticationResource(UserSessionData userSessionData, AuthenticationService authenticationService) {
        this.userSessionData = userSessionData;
        this.authenticationService = authenticationService;
    }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, LoginRequest loginRequest)
        throws InternalApplicationException, InvalidInputException {
        var login = loginRequest.getLogin();
        var password = loginRequest.getPassword();
        if (login == null || password == null) {
            throw new InvalidInputException("Both login and password must be provided");
        }
        if (login.length() > MAX_LOG_PASSWD_LENGTH || password.length() > MAX_LOG_PASSWD_LENGTH) {
            throw new InvalidInputException("Both login and password should be less than "
                    + MAX_LOG_PASSWD_LENGTH + " characters");
        }
        var userEntity = authenticationService.checkUserCredentials(login, password.toCharArray());
        if (userEntity != null) {
            invalidateRequestSession(request);
            var session = request.getSession(true);
            session.setMaxInactiveInterval(loginRequest.isRememberMe() ? -1 : HALF_HOUR_SECONDS);
            var loginData = new LoginData(userEntity.getId(), userEntity.getLogin());
            userSessionData.registerLogin(loginData);
            return Response.ok().build();
        }
        LOG.warn("Failed to login using {}", loginRequest);
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("logout")
    public Response logout(@Context HttpServletRequest request) {
        userSessionData.registerLogout();
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

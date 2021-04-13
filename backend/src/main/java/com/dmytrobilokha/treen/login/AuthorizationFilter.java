package com.dmytrobilokha.treen.login;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> SECURED_PATHS = Set.of("/api/");

    private UserSessionData userSessionData;

    public AuthorizationFilter() {
        //Framework
    }

    @Inject
    public AuthorizationFilter(UserSessionData userSessionData) {
        this.userSessionData = userSessionData;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (userSessionData.getLoginData() != null) {
            chain.doFilter(request, response);
            return;
        }
        var httpRequest = (HttpServletRequest) request;
        var requestPath = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        var sensitiveRequest = SECURED_PATHS
                .stream()
                .anyMatch(requestPath::startsWith);
        if (!sensitiveRequest) {
            chain.doFilter(request, response);
            return;
        }
        var httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must authenticate first");
    }

}

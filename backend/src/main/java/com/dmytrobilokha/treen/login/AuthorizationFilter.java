package com.dmytrobilokha.treen.login;

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

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        if (isUserAuthenticated(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }
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

    private boolean isUserAuthenticated(HttpServletRequest request) {
        var session = request.getSession(false);
        return session != null && session.getAttribute("userData") != null;
    }

}

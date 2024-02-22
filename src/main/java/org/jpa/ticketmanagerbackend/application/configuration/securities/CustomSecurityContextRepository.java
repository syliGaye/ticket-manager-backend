package org.jpa.ticketmanagerbackend.application.configuration.securities;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class CustomSecurityContextRepository implements SecurityContextRepository {

    private static final String TICKET_CONTEXT_KEY = "TICKET_CONTEXT_KEY";

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        /**
        SecurityContext context = null;
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // Check if there is a saved request, and if so, clear it from the cache
        if (savedRequest != null) {
            requestCache.removeRequest(request, response);
        }

        // Get the security context from the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object contextObj = session.getAttribute(TICKET_CONTEXT_KEY);
            if (contextObj instanceof SecurityContext) context = (SecurityContext) contextObj;
        }

        return context;
         **/

        HttpSession session = requestResponseHolder.getRequest().getSession(false);
        if (session != null) {
            Object contextObj = session.getAttribute(TICKET_CONTEXT_KEY);
            if (contextObj instanceof SecurityContext) {
                return (SecurityContext) contextObj;
            }
        }
        return new HttpSessionSecurityContextRepository().loadContext(requestResponseHolder);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null && context != null) {
            session.setAttribute(TICKET_CONTEXT_KEY, context);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(TICKET_CONTEXT_KEY) != null;
    }

    public void removeContext(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(TICKET_CONTEXT_KEY) != null) {
            session.removeAttribute(TICKET_CONTEXT_KEY);
        }
    }
}
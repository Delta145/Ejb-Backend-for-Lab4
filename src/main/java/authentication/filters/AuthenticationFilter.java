package authentication.filters;

import annotations.AuthenticatedUser;
import annotations.Secured;
import entities.User;
import services.UserService;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    @AuthenticatedUser
    private Event<String> userAuthenticatedEvent;

    @EJB
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic "))
            throw new NotAuthorizedException("Authorization header must be provided");

        String token = authorizationHeader.substring("Basic".length()).trim();

        try {
            validateToken(token);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validateToken(String token) {
        User user = userService.findUserByAuthToken(token);
        if (user == null)
            throw new NotAuthorizedException("Bad auth token");
        userAuthenticatedEvent.fire(user.getUsername());
    }
}

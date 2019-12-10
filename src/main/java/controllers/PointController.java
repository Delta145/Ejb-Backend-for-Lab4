package controllers;

import annotations.AuthenticatedUser;
import annotations.Secured;
import entities.Point;
import entities.User;
import services.PointService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/points")
@RequestScoped
public class PointController {

    @EJB
    private PointService pointService;

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    @PostConstruct
    public void init() {
        // Надо переопределить объект пользователя, контейнер инжектит прокси вместо оригинального объекта...
        authenticatedUser = new User(authenticatedUser.getUsername(), authenticatedUser.getPasswordHash(), authenticatedUser.getAuthToken());
    }

    @Secured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Point> getAllPoints() {
        return pointService.getAllPoints(authenticatedUser);
    }

    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newPoint(Point point) {
        point.setUser(authenticatedUser);
        point = pointService.save(point);
        return Response.ok(point).build();
    }

    @Secured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("recalculate")
    public List<Point> getAllPointsRecalculated(@QueryParam("r") double r) {
        return pointService.getAllPointsRecalculated(authenticatedUser, r);
    }

}

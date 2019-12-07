package controllers;

import annotations.Secured;
import entities.Point;
import entities.User;
import services.PointService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/points")
@Stateless
public class PointController {

    @EJB
    private PointService pointService;

    @Secured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Point> getAllPoints(User user) {
        return pointService.getAllPoints(user);
    }

    @Secured
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void newPoint(Point point, User user) {
        point.setUser(user);
        pointService.save(point);
    }

    @Secured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("recalculate")
    public List<Point> getAllPoints(User user,
                                    @QueryParam("r") double r) {
        return pointService.getAllPoints(user);
    }

}

package controllers;

import entities.Point;
import entities.User;
import services.PointService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/api/points")
@Stateless
public class PointController {

    @EJB
    private PointService pointService;

    @GET
    public List<Point> getAllPoints(User user) {
        return pointService.getAllPoints(user);
    }

    @POST
    public void newPoint(Point point, User user) {
        point.setUser(user);
        pointService.save(point);
    }

    @GET
    @Path("/recalculate")
    public List<Point> getAllPoints(User user,
                                    @QueryParam("r") double r) {
        return pointService.getAllPoints(user);
    }

}

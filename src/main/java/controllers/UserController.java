package controllers;

import entities.User;
import services.UserService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/users")
@Stateless
public class UserController {

    @EJB
    private UserService userService;

    @Path("register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@FormParam( "username" ) String username,
                             @FormParam( "password" ) String password,
                             @FormParam( "passwordRepeat" ) String passwordRepeat) {



        return null;
    }

    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @Context HttpHeaders httpHeaders,
            @FormParam( "username" ) String username,
            @FormParam( "password" ) String password) {
        User user = userService.findUserByUsername(username);


        return null;
    }

}

package controllers;

import annotations.Secured;
import data.ResponseMessage;
import data.UserCredentials;
import entities.User;
import lombok.SneakyThrows;
import services.UserService;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Path("/users")
@Stateless
public class UserController {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    @EJB
    private UserService userService;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(UserCredentials userCredentials) {
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();

        try {
            if (username == null || password == null)
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        new ResponseMessage("Username or password is null")).build();

            authenticate(username, getPasswordHash(password));

            String token = issueToken(username);

            return Response.ok(new ResponseMessage(token)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    new ResponseMessage("Wrong user or password")).build();
        }
    }

    @Secured
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("logout")
    public Response logout(@HeaderParam("Authorization") String authorization) {
        String token = authorization.substring("Basic".length());
        try {
            userService.invalidateToken(userService.findUserByAuthToken(token).getUsername());
            return Response.ok(new ResponseMessage("logout successful")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response register(UserCredentials userCredentials) {
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();

        if (username == null || password == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    new ResponseMessage("Username or password is null")).build();

        if (userService.findUserByUsername(username) != null)
            return Response.status(Response.Status.CONFLICT).entity(
                    new ResponseMessage("User already exists")).build();

        String passwordHash = getPasswordHash(password);

        User user = new User(username, passwordHash, null);
        userService.saveOrUpdateUser(user);

        return Response.ok(new ResponseMessage("User successfully registered")).build();
    }

    private void authenticate(String username, String password) throws AuthenticationException {
        User user = userService.findUserByUsername(username);

        if (user == null)
            throw new AuthenticationException("User not found");
        if (!user.getPasswordHash().equals(password))
            throw new AuthenticationException("Passwords aren't equal");
    }

    private String issueToken(String username) {
        String token = generateNewToken();
        User user = userService.findUserByUsername(username);
        user.setAuthToken(token);
        userService.saveOrUpdateUser(user);
        return token;
    }


    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    @SneakyThrows
    private static String getPasswordHash(String password) {
        byte[] stub = new byte[16];
        KeySpec spec = new PBEKeySpec(password.toCharArray(), stub, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        return base64Encoder.encodeToString(hash);
    }
}

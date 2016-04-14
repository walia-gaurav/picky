package org.cmu.picky.resources;

import org.cmu.picky.model.User;
import org.cmu.picky.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class UserResource extends BaseResource {

    private static UserService userService;

    public static void init(UserService _userService) {
        userService = _userService;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("username") final String username, @FormParam("password") final String password) {
        User user = userService.login(username, password);

        if (user != null) {
            return Response.ok(user).status(Response.Status.OK).build();
        } else {
            return Response.ok().status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout() {
        if (principal != null) {
            userService.logout(principal.getName());
        }
        return Response.ok().build();
    }

}

/**
 * 
 */
package com.genie.account.mgmt.resources;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.core.UserManager;
import com.genie.account.mgmt.util.RegisterRequestJSON;
import com.genie.account.mgmt.util.FacebookGraphAPIResponseJSON;
import com.genie.account.mgmt.util.RegisterResponseJSON;
import com.genie.heartrate.mgmt.util.Formatter;
import com.genie.mgmt.GoodResponseObject;

/**
 * @author manojkumar
 *
 */
@Path("/Users")
@Component
public class UserResource 
{
	@Autowired
	@Qualifier("userManagerMySQLImpl")
	private UserManager userManager;
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}
	
	@GET
	@Path("{email}")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserInfo(@PathParam("email") String email, @Context UriInfo uriInfo)
	{
		User user = userManager.getUserInformation(email);
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), user);
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
		}
	}
	
	@POST
	@Path("register")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String registerUser(RegisterRequestJSON requestJson )
	{
		GoodResponseObject gro = null;
		FacebookGraphAPIResponseJSON responseJson = userManager.authenticateUser(requestJson);
		User existingUser = userManager.getUserInformation(responseJson.getEmail());
		if(null == existingUser){		
			User newUser = new User();
			newUser.setUserid(UUID.randomUUID().toString());
			newUser.setEmail(responseJson.getEmail());
			newUser.setFirstName(responseJson.getName());
			userManager.registerUser(newUser);
			RegisterResponseJSON regResponseJson = new RegisterResponseJSON();
			regResponseJson.setUserid(newUser.getUserid());
			gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(),regResponseJson);			
			try {
				return Formatter.getAsJson(gro, false);
			} catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
			}
		}
		else{
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), "User already exists");			
			try {
				return Formatter.getAsJson(gro, false);
			} catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
			}
		}		
	}		
}

/**
 * 
 */
package com.genie.account.mgmt.resources;

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
	@Path("create")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String createUser(@Context UriInfo uriInfo)
	{
		return "";
	}		
}

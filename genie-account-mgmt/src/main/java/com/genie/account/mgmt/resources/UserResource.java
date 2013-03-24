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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * @author manojkumar
 *
 */
@Path("/Users")
public class UserResource 
{
	@GET
	@Path("{email}")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserInfo(@PathParam("email") String email, @Context UriInfo uriInfo)
	{
		return "";
	}
	
	@POST
	@Path("create")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String createUser(@Context UriInfo uriInfo)
	{
		return "";
	}
	
	@GET
	@Path("check")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String checkUserNameAvailability(@Context UriInfo uriInfo)
	{
		return "";
	}
}

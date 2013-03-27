/**
 * 
 */
package com.genie.heartrate.mgmt.resources;

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

@Path("/HeartRateTests")
public class HeartRateTestResource 
{

	@GET
	@Path("{userid}")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String getHeartRateTests(@PathParam("userid") String userid, @Context UriInfo uriInfo)
	{
		return "";
	}
	
	@POST
	@Path("save/{userid}")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String createUser(@PathParam("userid") String userid, @Context UriInfo uriInfo)
	{
		return "";
	}
}

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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.genie.heartrate.mgmt.GoodResponseObject;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.core.HeartRateMgmt;
import com.genie.heartrate.mgmt.impl.HeartRateMgmtMySQLImpl;
import com.genie.heartrate.mgmt.util.Formatter;

/**
 * @author manojkumar
 *
 */

@Path("/HeartRateZones")
public class HeartRateZoneResource 
{
	
	@GET
	@Path("{userid}")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String getHeartRateZones(@PathParam("userid") String userid, @Context UriInfo uriInfo)
	{
		try
		{
			HeartRateMgmt heartRateMgmt = new HeartRateMgmtMySQLImpl();
			UserHeartRateZone heartRateZone = heartRateMgmt.getHeartRateZonesForUser(Long.parseLong(userid));
			GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), heartRateZone);
			return Formatter.getAsJson(gro, true);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}
	
}

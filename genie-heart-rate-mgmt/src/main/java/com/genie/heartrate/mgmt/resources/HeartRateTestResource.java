/**
 * 
 */
package com.genie.heartrate.mgmt.resources;

import java.util.Calendar;

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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.core.HeartRateMgmt;
import com.genie.heartrate.mgmt.util.Formatter;
import com.genie.mgmt.GoodResponseObject;

/**
 * @author manojkumar
 *
 */

@Path("/HeartRateTests")
@Component
public class HeartRateTestResource 
{
	
	@Autowired
	@Qualifier("heartRateMgmtMySQLImpl")
	private HeartRateMgmt heartRateMgmt;
	
	public HeartRateMgmt getHeartRateMgmt()
	{
		return this.heartRateMgmt;
	}
	
	public void setHeartRateMgmt(HeartRateMgmt heartRateMgmt)
	{
		this.heartRateMgmt = heartRateMgmt;
	}
	

	@GET
	@Path("{userid}/results")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String getHeartRateTests(@PathParam("userid") String userid, @Context UriInfo uriInfo)
	{
		try
		{
			UserHeartRateTest heartRateTest = heartRateMgmt.getHeartRateTestResultsForUser(Long.parseLong(userid));
			GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), heartRateTest);
			return Formatter.getAsJson(gro, true);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}
	
	@POST
	@Path("{userid}/save")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String saveHeartRateTests(@PathParam("userid") String userid, UserHeartRateTest uhrt)
	{
		try
		{
			uhrt.setUserid(Long.valueOf(userid));
			
			if(null != uhrt.getRestingHeartRate()){
				uhrt.setRestingHeartRateTimestamp(Calendar.getInstance());	
			}
			
			if(null != uhrt.getMaximalHeartRate()){
				uhrt.setMaximalHeartRateTimestamp(Calendar.getInstance());	
			}
			
			if(null != uhrt.getThresholdHeartRate()){
				uhrt.setThresholdHeartRateTimestamp(Calendar.getInstance());
			}
			
			/*heartRateMgmt.saveHeartRateTestResultsForUser(Long.parseLong(userid), "");*/
			GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase());
			return Formatter.getAsJson(gro, false);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}
}

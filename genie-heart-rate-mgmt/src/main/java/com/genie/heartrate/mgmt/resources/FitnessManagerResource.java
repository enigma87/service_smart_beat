package com.genie.heartrate.mgmt.resources;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.core.FitnessManager;
import com.genie.heartrate.mgmt.json.SaveTrainingSessionRequestJson;
import com.genie.heartrate.mgmt.json.SaveTrainingSessionResponseJson;
import com.genie.heartrate.mgmt.json.ShapeIndexResponseJson;
import com.genie.heartrate.mgmt.util.Formatter;
import com.genie.mgmt.GoodResponseObject;

@Path("/FitnessManager")
@Component
public class FitnessManagerResource {

		@Autowired
		@Qualifier("fitnessManagerMySQLImpl")
		private FitnessManager fitnessManager;
		
		public FitnessManager getFitnessManager()
		{
			return this.fitnessManager;
		}
		
		public void setFitnessManager(FitnessManager fitnessManager)
		{
			this.fitnessManager = fitnessManager;
		}
		

		@GET
		@Path("{userid}/shapeIndex")
		@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
		@Produces(MediaType.APPLICATION_JSON)
		public String getShapeIndex(@PathParam("userid") String userid,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType)
		{
			 
			/*Double shapeIndex = fitnessManager.getFitnessShapeIndex(fitnessManager.getRecentTrainingSessionId(userid));
			ShapeIndexResponseJson shapeIndexResponseJson = new ShapeIndexResponseJson();
			shapeIndexResponseJson.setUserid(userid);
			shapeIndexResponseJson.setShapeIndex(shapeIndex);*/
			
			ShapeIndexResponseJson shapeIndexResponseJson = new ShapeIndexResponseJson();
			shapeIndexResponseJson.setUserid(userid);
			shapeIndexResponseJson.setShapeIndex(100.0);
			
			GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), shapeIndexResponseJson);
			try
			{				
				return Formatter.getAsJson(gro, true);
			}
			catch(Exception ex)
			{
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
			}
		}
		
		@POST
		@Path("{userid}/saveFitnessTrainingSession")
		@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
		@Produces(MediaType.APPLICATION_JSON)
		public String saveFitnessTrainingSession(SaveTrainingSessionRequestJson saveTrainingSessionRequestJson)
		{

			/*FitnessTrainingSessionBean fitnessTrainingSessionBean = saveTrainingSessionRequestJson.getAsTrainingSessionBean();
			
			fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
			
			Double shapeIndex = fitnessManager.getFitnessShapeIndex(fitnessTrainingSessionBean.getTrainingSessionId());
			
			SaveTrainingSessionResponseJson saveTrainingSessionResponseJson = new SaveTrainingSessionResponseJson();
			saveTrainingSessionResponseJson.setUserid(saveTrainingSessionRequestJson.getUserid());
			saveTrainingSessionResponseJson.setShapeIndex(shapeIndex);*/
			
			SaveTrainingSessionResponseJson saveTrainingSessionResponseJson = new SaveTrainingSessionResponseJson();
			saveTrainingSessionResponseJson.setUserid(saveTrainingSessionRequestJson.getUserid());
			saveTrainingSessionResponseJson.setShapeIndex(100.0);
			
			GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(),saveTrainingSessionResponseJson);
			try
			{
 
				return Formatter.getAsJson(gro, false);
			}
			catch(Exception ex)
			{
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
			}
		}
	


}

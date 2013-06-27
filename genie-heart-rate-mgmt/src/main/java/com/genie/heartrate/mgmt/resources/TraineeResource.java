/**
 * 
 */
package com.genie.heartrate.mgmt.resources;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.core.UserManager;
import com.genie.account.mgmt.json.RegisterRequestJSON;
import com.genie.account.mgmt.json.RegisterResponseJSON;
import com.genie.account.mgmt.json.UserInfoJSON;
import com.genie.account.mgmt.util.AuthenticationStatus;
import com.genie.account.mgmt.util.Formatter;
import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.core.FitnessManager;
import com.genie.heartrate.mgmt.json.SaveFitnessTrainingSessionRequestJson;
import com.genie.heartrate.mgmt.json.SaveFitnessTrainingSessionResponseJson;
import com.genie.heartrate.mgmt.json.ShapeIndexResponseJson;
import com.genie.mgmt.GoodResponseObject;

/**
 * @author dhasarathy
 *
 */
@Path("/trainee")
@Component
public class TraineeResource 
{
	@Autowired
	@Qualifier("userManagerMySQLImpl")
	private UserManager userManager;
	
	@Autowired
	@Qualifier("fitnessManagerMySQLImpl")
	private FitnessManager fitnessManager;
	
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}
	
	public FitnessManager getFitnessManager(){
		return this.fitnessManager;
	}
	
	public void setFitnessManager(FitnessManager fitnessManager){
		this.fitnessManager = fitnessManager;
	}

	
	@GET
	@Path("{email}")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserInfo(@PathParam("email") String email, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType){
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		if(AuthenticationStatus.AUTHENTICATION_STATUS_APPROVED.equals(authStatus.getAuthenticationStatus())){
			User user = userManager.getUserInformationByEmail(email);
			UserInfoJSON userInfoJSON = new UserInfoJSON();
			userInfoJSON.copyFromUserBean(user);
			GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), userInfoJSON);
			try {
				return Formatter.getAsJson(gro, true);
			} catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
			}
		}else{
			GoodResponseObject gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), "Invalid access token");
			try {
				return Formatter.getAsJson(gro, true);
			} catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
			}
		}
	}
	
	@POST
	@Path("register")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String registerUser(RegisterRequestJSON requestJson){
		GoodResponseObject gro = null;
		AuthenticationStatus authStatus = userManager.authenticateRequest(requestJson.getAccessToken(), requestJson.getAccessTokenType());
		if(AuthenticationStatus.AUTHENTICATION_STATUS_DENIED.equals(authStatus.getAuthenticationStatus())){
			if(null == authStatus.getAuthenticatedUser()){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), "Invalid access token");			
				try {
					return Formatter.getAsJson(gro, false);
				} catch (Exception e) {
					throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
				}	
			}
			else{
				User newUser = authStatus.getAuthenticatedUser();
				newUser.setUserid(UUID.randomUUID().toString());
				userManager.registerUser(authStatus.getAuthenticatedUser());
				RegisterResponseJSON regResponseJson = new RegisterResponseJSON();
				regResponseJson.setUserid(newUser.getUserid());
				gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(),regResponseJson);			
				try {
					return Formatter.getAsJson(gro, false);
				} catch (Exception e) {
					throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
				}
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
	
	@GET
	@Path("{userid}/shapeIndex")
	@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String getShapeIndex(@PathParam("userid") String userid,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType){
		 
		Double shapeIndex = fitnessManager.getFitnessShapeIndex(fitnessManager.getRecentTrainingSessionId(userid));
		ShapeIndexResponseJson shapeIndexResponseJson = new ShapeIndexResponseJson();
		shapeIndexResponseJson.setUserid(userid);
		shapeIndexResponseJson.setShapeIndex(shapeIndex);
			
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
	@Path("{userid}/fitnessTrainingSession/save")
	@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String saveFitnessTrainingSession(@PathParam("userid") String userid,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType ,SaveFitnessTrainingSessionRequestJson saveTrainingSessionRequestJson){

		saveTrainingSessionRequestJson.setUserid(userid);
		FitnessTrainingSessionBean fitnessTrainingSessionBean = saveTrainingSessionRequestJson.getAsTrainingSessionBean();
		
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		
		String fitnessTrainingSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		Double shapeIndex = fitnessManager.getFitnessShapeIndex(fitnessTrainingSessionId);
		
		SaveFitnessTrainingSessionResponseJson saveFitnessTrainingSessionResponseJson = new SaveFitnessTrainingSessionResponseJson();
		saveFitnessTrainingSessionResponseJson.setUserid(saveTrainingSessionRequestJson.getUserid());		
		saveFitnessTrainingSessionResponseJson.setTrainingSessionId(fitnessTrainingSessionBean.getTrainingSessionId());
		saveFitnessTrainingSessionResponseJson.setShapeIndex(shapeIndex);
		saveFitnessTrainingSessionResponseJson.setTrainingSessionId(fitnessTrainingSessionId);
		
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(),saveFitnessTrainingSessionResponseJson);
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

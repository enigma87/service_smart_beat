/**
 * 
 */
package com.genie.smartbeat.resources;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.core.TrainingSessionValidityStatus;
import com.genie.smartbeat.json.HeartRateZoneResponseJson;
import com.genie.smartbeat.json.SaveFitnessTrainingSessionRequestJson;
import com.genie.smartbeat.json.SaveFitnessTrainingSessionResponseJson;
import com.genie.smartbeat.json.SaveHeartrateTestRequestJson;
import com.genie.smartbeat.json.SaveHeartrateTestResponseJson;
import com.genie.smartbeat.json.ShapeIndexHistoryResponseJson;
import com.genie.smartbeat.json.ShapeIndexResponseJson;
import com.genie.smartbeat.json.TrainingSessionByIdResponseJson;
import com.genie.smartbeat.json.TrainingSessionIdsByRangeResponseJson;
import com.genie.smartbeat.json.TrainingSessionsByRangeResponseJson;
import com.genie.social.beans.UserBean;
import com.genie.social.core.AuthenticationStatus;
import com.genie.social.core.UserManager;
import com.genie.social.json.RegisterRequestJSON;
import com.genie.social.json.RegisterResponseJSON;
import com.genie.social.json.UserInfoJSON;
import com.genie.social.util.Formatter;

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
	@Path("email/{email}")
	@Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserInfo(@PathParam("email") String email, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType){
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro;
		if(null != authStatus 
				&& AuthenticationStatus.Status.APPROVED.equals(authStatus.getAuthenticationStatus())) {
			UserBean user = userManager.getUserInformationByEmail(email);
			if (null != user) {
				UserInfoJSON userInfoJSON = new UserInfoJSON();
				userInfoJSON.copyFromUserBean(user);
				gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), userInfoJSON);
			} else {
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), "Unknown User!");
			}
		} else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), "Invalid access token");
		}
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
	public String registerUser(RegisterRequestJSON requestJson){
		GoodResponseObject gro = null;
		AuthenticationStatus authStatus = userManager.authenticateRequest(requestJson.getAccessToken(), requestJson.getAccessTokenType());
		if(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatus())) {
			if(null == authStatus.getAuthenticatedUser()){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), "Invalid access token");			
				try {
					return Formatter.getAsJson(gro, false);
				} catch (Exception e) {
					throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
				}	
			}
			else{
				UserBean newUser = authStatus.getAuthenticatedUser();
				newUser.setUserid(UUID.randomUUID().toString());
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
		} 
		else if (AuthenticationStatus.Status.DENIED_EMAIL_REQUIRED.equals(authStatus.getAuthenticationStatus())) {

			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), "Request Error:" + AuthenticationStatus.Status.DENIED_EMAIL_REQUIRED);
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
	
	@GET
	@Path("id/{userid}/shapeIndex")
	@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String getShapeIndex(@PathParam("userid") String userid,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType){
		 
		Double shapeIndex = fitnessManager.getShapeIndex(fitnessManager.getRecentTrainingSessionId(userid));
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
	
	@GET
	@Path("id/{userid}/shapeIndex/inTimeInterval")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getShapeIndexHistoryInInterval (@PathParam("userid") String userID, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		List<FitnessShapeIndexBean> shapeIndexBeans = fitnessManager.getShapeIndexHistoryInTimeInterval(userID, startTimeStamp, endTimeStamp);
		ShapeIndexHistoryResponseJson shapeIndexHistoryJson = new ShapeIndexHistoryResponseJson();
		shapeIndexHistoryJson.setShapeIndexes(shapeIndexBeans);
		shapeIndexHistoryJson.setUserID(userID);
		
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), shapeIndexHistoryJson);
		
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}
	
	@GET
	@Path("id/{userid}/heartrateZones")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getHeartrateZones(@PathParam("userid") String userID, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {

		double[][] heartrateZones = fitnessManager.getHeartrateZones(userID);
		HeartRateZoneResponseJson heartRateZoneJson = new HeartRateZoneResponseJson(); 
		heartRateZoneJson.setUserid(userID);
		heartRateZoneJson.setHeartrateZones(heartrateZones);
		
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), heartRateZoneJson);
	
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}
	
	@GET
	@Path("id/{userid}/trainingSessionId/inTimeInterval")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getFitnessTrainingSessionIds(@PathParam("userid") String userID, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		List<String> sessionIDs= fitnessManager.getTrainingSessionIdsInTimeInterval(userID, startTimeStamp, endTimeStamp);
		TrainingSessionIdsByRangeResponseJson trainingSessionIdRangeJson = new TrainingSessionIdsByRangeResponseJson();
		trainingSessionIdRangeJson.setUserID(userID);
		trainingSessionIdRangeJson.setTrainingSessionIDs(sessionIDs);
		
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), trainingSessionIdRangeJson);
		
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}

	@GET
	@Path("id/{userid}/trainingSession/inTimeInterval")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getFitnessTrainingSessions(@PathParam("userid") String userID, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		List<FitnessTrainingSessionBean> trainingSessions= fitnessManager.getTrainingSessionsInTimeInterval(userID, startTimeStamp, endTimeStamp);
		TrainingSessionsByRangeResponseJson trainingSessionRangeJson = new TrainingSessionsByRangeResponseJson();
		trainingSessionRangeJson.setUserID(userID);
		trainingSessionRangeJson.setTrainingSessionBeans(trainingSessions);
		
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), trainingSessionRangeJson);
		
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}

	@GET
	@Path("id/{userid}/trainingSession")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getFitnessTrainingSessionById(@PathParam("userid") String userID, @QueryParam("trainingSessionID") String trainingSessionID, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType){
		
		TrainingSessionByIdResponseJson trainingSessionResponseJson = new TrainingSessionByIdResponseJson();
		FitnessTrainingSessionBean trainingSessionBean = fitnessManager.getTrainingSessionById(trainingSessionID);
		GoodResponseObject gro;
		if (null != trainingSessionBean) {
			trainingSessionResponseJson.noJSONSetTrainingSession(trainingSessionBean);
			gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), trainingSessionResponseJson);
		} else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), Status.NOT_ACCEPTABLE.getReasonPhrase(), null);
		}
			
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}

	@POST
	@Path("id/{userid}/trainingSession/save")
	@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String saveFitnessTrainingSession(@PathParam("userid") String userid,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType ,SaveFitnessTrainingSessionRequestJson saveTrainingSessionRequestJson){

		saveTrainingSessionRequestJson.setUserid(userid);
		FitnessTrainingSessionBean fitnessTrainingSessionBean = saveTrainingSessionRequestJson.getAsTrainingSessionBean();
		
		GoodResponseObject gro = null;
		
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		
		if (TrainingSessionValidityStatus.VALID.equals(fitnessTrainingSessionBean.getValidityStatus())) {
			String fitnessTrainingSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
			Double shapeIndex = fitnessManager.getShapeIndex(fitnessTrainingSessionId);
		
			SaveFitnessTrainingSessionResponseJson saveFitnessTrainingSessionResponseJson = new SaveFitnessTrainingSessionResponseJson();
			saveFitnessTrainingSessionResponseJson.setUserid(saveTrainingSessionRequestJson.getUserid());		
			saveFitnessTrainingSessionResponseJson.setTrainingSessionId(fitnessTrainingSessionBean.getTrainingSessionId());
			saveFitnessTrainingSessionResponseJson.setShapeIndex(shapeIndex);
			saveFitnessTrainingSessionResponseJson.setTrainingSessionId(fitnessTrainingSessionId);
			saveFitnessTrainingSessionResponseJson.setvDot(fitnessTrainingSessionBean.getVdot());
			
			FitnessHomeostasisIndexBean fitnessHomeostasisIndexModel = fitnessManager.getHomeostasisIndexModelForUser(fitnessTrainingSessionBean.getUserid());
			saveFitnessTrainingSessionResponseJson.setRecentMinimumOfHomeostasisIndex(fitnessHomeostasisIndexModel.getRecentMinimumOfHomeostasisIndex());
			saveFitnessTrainingSessionResponseJson.setRecentTotalLoadOfExercise(fitnessHomeostasisIndexModel.getRecentTotalLoadOfExercise());
			saveFitnessTrainingSessionResponseJson.setTraineeClassification(fitnessHomeostasisIndexModel.getTraineeClassification());
			gro = new GoodResponseObject(Status.OK.getStatusCode(), fitnessTrainingSessionBean.getValidityStatus().toString() ,saveFitnessTrainingSessionResponseJson);
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), fitnessTrainingSessionBean.getValidityStatus().toString());
		}
		
		try {
			return Formatter.getAsJson(gro, false);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}

	@POST
	@Path("id/{userid}/heartrateTest/save")
	@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String saveHeartrateTest(@PathParam("userid") String userid,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType ,SaveHeartrateTestRequestJson saveHeartrateTestRequestJson){
		FitnessHeartrateTestBean heartrateTestBean = saveHeartrateTestRequestJson.getAsHeartrateTestBean();
		heartrateTestBean.setUserid(userid);
		fitnessManager.saveHeartrateTest(heartrateTestBean);
		
		Double shapeIndex = fitnessManager.getShapeIndex(fitnessManager.getRecentTrainingSessionId(userid));
		ShapeIndexResponseJson shapeIndexResponseJson = new ShapeIndexResponseJson();
		shapeIndexResponseJson.setUserid(userid);
		shapeIndexResponseJson.setShapeIndex(shapeIndex);
		
		double[][] heartrateZones = fitnessManager.getHeartrateZones(userid);
		HeartRateZoneResponseJson heartRateZoneJson = new HeartRateZoneResponseJson(); 
		heartRateZoneJson.setUserid(userid);
		heartRateZoneJson.setHeartrateZones(heartrateZones);
		
		SaveHeartrateTestResponseJson saveHeartrateTestResponseJson = new SaveHeartrateTestResponseJson();
		saveHeartrateTestResponseJson.setHeartrateZones(heartRateZoneJson);
		saveHeartrateTestResponseJson.setShapeIndex(shapeIndexResponseJson);
		
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(),saveHeartrateTestResponseJson);
		try
		{
			return Formatter.getAsJson(gro, false);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}
	
	@DELETE
	@Path("id/{userid}/data/clear")
	@Consumes({MediaType.TEXT_HTML})
	@Produces(MediaType.TEXT_HTML)
	public String clearTraineeData(@PathParam("userid") String userid){

		fitnessManager.clearTraineeData(userid);
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase());
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
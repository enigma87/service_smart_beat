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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionIdBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.core.errors.AccessTokenError;
import com.genie.smartbeat.core.errors.HeartrateTestErrors;
import com.genie.smartbeat.core.errors.HomeostasisModelErrors;
import com.genie.smartbeat.core.errors.TimeErrors;
import com.genie.smartbeat.core.errors.TrainingSessionErrors;
import com.genie.smartbeat.core.errors.UserErrors;
import com.genie.smartbeat.core.exceptions.homeostasis.AbsenceOfHomeostasisIndexModelException;
import com.genie.smartbeat.core.exceptions.homeostasis.HomeostasisModelException;
import com.genie.smartbeat.core.exceptions.session.AbsenceOfTrainingSessionException;
import com.genie.smartbeat.core.exceptions.session.InvalidSpeedDistributionException;
import com.genie.smartbeat.core.exceptions.session.InvalidTimeDistributionException;
import com.genie.smartbeat.core.exceptions.session.TrainingSessionException;
import com.genie.smartbeat.core.exceptions.test.HeartrateTestException;
import com.genie.smartbeat.core.exceptions.test.InvalidHeartrateException;
import com.genie.smartbeat.core.exceptions.time.InvalidDurationException;
import com.genie.smartbeat.core.exceptions.time.InvalidEndTimestampException;
import com.genie.smartbeat.core.exceptions.time.InvalidStartTimestampException;
import com.genie.smartbeat.core.exceptions.time.InvalidTimestampException;
import com.genie.smartbeat.core.exceptions.time.InvalidTimestampInChronologyException;
import com.genie.smartbeat.core.exceptions.time.TimeException;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.json.HeartRateZoneResponseJson;
import com.genie.smartbeat.json.HeartrateTestByRangeResponseJson;
import com.genie.smartbeat.json.HomeostasisIndexResponseJson;
import com.genie.smartbeat.json.RecoveryTimeResponseJson;
import com.genie.smartbeat.json.SaveFitnessTrainingSessionRequestJson;
import com.genie.smartbeat.json.SaveFitnessTrainingSessionResponseJson;
import com.genie.smartbeat.json.SaveHeartrateTestRequestJson;
import com.genie.smartbeat.json.SaveHeartrateTestResponseJson;
import com.genie.smartbeat.json.ShapeIndexHistoryResponseJson;
import com.genie.smartbeat.json.ShapeIndexResponseJson;
import com.genie.smartbeat.json.TraineeIdsResponseJson;
import com.genie.smartbeat.json.TrainingSessionByIdResponseJson;
import com.genie.smartbeat.json.TrainingSessionIdsByRangeResponseJson;
import com.genie.smartbeat.json.TrainingSessionsByRangeResponseJson;
import com.genie.social.beans.UserBean;
import com.genie.social.beans.UserIdBean;
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
	private static final Logger log = Logger.getLogger(TraineeResource.class);
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
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), UserErrors.USER_UNKNOWN.toString());
			}
		} else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), AccessTokenError.ACCESS_TOKEN_INVALID.toString());
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
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), AccessTokenError.ACCESS_TOKEN_INVALID.toString());			
				try {
					return Formatter.getAsJson(gro, false);
				} catch (Exception e) {
					throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
				}	
			}
			else{
				UserBean newUser = authStatus.getAuthenticatedUser();
				
				/* expect dob and gender from app, if fb privacy settings disallow */
				UserBean newJsonUser = requestJson.getAsUserBean();
				if (null == newUser.getDob()) {
					newUser.setDob(newJsonUser.getDob());
				}
				if (null == newUser.getGender()) {
					newUser.setGender(newJsonUser.getGender());
				}
				
				/*Smartbeat level validation*/
				if(null == newUser.getDob() || null == newUser.getGender()){
					gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), UserErrors.USER_DOB_AND_GENDER_REQUIRED.toString());
					try {
						return Formatter.getAsJson(gro, false);
					} catch (Exception e) {
						throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
					}
				}else{	
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
		} 
		else if (AuthenticationStatus.Status.DENIED_EMAIL_REQUIRED.equals(authStatus.getAuthenticationStatus())) {

			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), UserErrors.USER_EMAIL_REQUIRED.toString());
			try {
				return Formatter.getAsJson(gro, false);
			} catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e).build());
			}		
		}
		else{
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), UserErrors.USER_ALREADY_EXISTS.toString());			
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
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {
		 
			Double shapeIndex = fitnessManager.getShapeIndex(fitnessManager.getRecentTrainingSessionId(userid));
			ShapeIndexResponseJson shapeIndexResponseJson = new ShapeIndexResponseJson();
			shapeIndexResponseJson.setUserid(userid);
			shapeIndexResponseJson.setShapeIndex(shapeIndex);
			
			gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), shapeIndexResponseJson);
		} 
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		
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
	public String getShapeIndexHistoryInInterval (@PathParam("userid") String userid, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
	    if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {
	         try{
			   List<FitnessShapeIndexBean> shapeIndexBeans = fitnessManager.getShapeIndexHistoryInTimeInterval(userid, startTimeStamp, endTimeStamp);
			   ShapeIndexHistoryResponseJson shapeIndexHistoryJson = new ShapeIndexHistoryResponseJson();
			   shapeIndexHistoryJson.setShapeIndexes(shapeIndexBeans);
			   shapeIndexHistoryJson.setUserID(userid);
			   gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), shapeIndexHistoryJson);
			 }catch(InvalidStartTimestampException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
				log.info("User - " + userid + " failed to get shape index history due to InvalidStartTimeException");				
			 }catch(InvalidEndTimestampException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
				log.info("User - " + userid + " failed to get shape index history due to InvalidEndTimestampException");				
			 }catch(InvalidDurationException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
				log.info("User - " + userid + " failed to get shape index history due to InvalidDurationException");			
			 }catch(TimeException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
				log.info("User - " + userid + " failed to get shape index history due to TimeException");
			}		
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		
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
	public String getHeartrateZones(@PathParam("userid") String userid, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {

		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {

			double[][] heartrateZones = fitnessManager.getHeartrateZones(userid);
			HeartRateZoneResponseJson heartRateZoneJson = new HeartRateZoneResponseJson(); 
			heartRateZoneJson.setUserid(userid);
			heartRateZoneJson.setHeartrateZones(heartrateZones);
		
			gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), heartRateZoneJson);
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		
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
	public String getFitnessTrainingSessionIds(@PathParam("userid") String userid, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {
			
           try{
			   List<FitnessTrainingSessionIdBean> sessionIDs= fitnessManager.getTrainingSessionIdsInTimeInterval(userid, startTimeStamp, endTimeStamp);
			   TrainingSessionIdsByRangeResponseJson trainingSessionIdRangeJson = new TrainingSessionIdsByRangeResponseJson();
			   trainingSessionIdRangeJson.setUserID(userid);
			   trainingSessionIdRangeJson.setTrainingSessionIDs(sessionIDs);
			   gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), trainingSessionIdRangeJson);
           }catch(InvalidStartTimestampException e){
        	  gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
        	  log.info("User - " + userid + " failed to get trainingSessionIds due to TimeException");      		   
           }catch(InvalidEndTimestampException e){
        	  gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
        	  log.info("User - " + userid + " failed to get trainingSessionIds due to InvalidEndTimestampException");       		   
           }catch(InvalidDurationException e){
   			  gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
   			  log.info("User - " + userid + " failed to get trainingSessionIds due to InvalidDurationException");   			  
   		   }catch(TimeException e){
   			  gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
   			  log.info("User - " + userid + " failed to get trainingSessionIds due to TimeException");   			  
   		   }
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		
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
	public String getFitnessTrainingSessionsInInterval(@PathParam("userid") String userid, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {
            try{
				List<FitnessTrainingSessionBean> trainingSessions= fitnessManager.getTrainingSessionsInTimeInterval(userid, startTimeStamp, endTimeStamp);
				TrainingSessionsByRangeResponseJson trainingSessionRangeJson = new TrainingSessionsByRangeResponseJson();
				trainingSessionRangeJson.setUserID(userid);
				trainingSessionRangeJson.setTrainingSessionBeans(trainingSessions);
				gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), trainingSessionRangeJson);
            }catch(InvalidStartTimestampException e){
            	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
            	log.info("User - " + userid + " failed to get trainingSessions due to InvalidStartTimestampException");    			
            }catch(InvalidEndTimestampException e){
            	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
            	log.info("User - " + userid + " failed to get trainingSessions due to InvalidEndTimestampException");    			
            }catch(InvalidDurationException e){
    			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
    			log.info("User - " + userid + " failed to get trainingSessions due to InvalidDurationException");    			
    		}catch(TimeException e){
    			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIMESTAMP.toString());
    			log.info("User - " + userid + " failed to get trainingSessions due to TimeException");    			
    		}    		
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		
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
	public String getFitnessTrainingSessionById(@PathParam("userid") String userid, @QueryParam("trainingSessionID") String trainingSessionID, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType){
		
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {

			TrainingSessionByIdResponseJson trainingSessionResponseJson = new TrainingSessionByIdResponseJson();
			FitnessTrainingSessionBean trainingSessionBean = fitnessManager.getTrainingSessionById(trainingSessionID);

			if (null != trainingSessionBean) {
				trainingSessionResponseJson.noJSONSetTrainingSession(trainingSessionBean);
				gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), trainingSessionResponseJson);
			} else {
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), Status.NOT_ACCEPTABLE.getReasonPhrase(), null);
			}
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
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

		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {
		
			saveTrainingSessionRequestJson.setUserid(userid);
			FitnessTrainingSessionBean fitnessTrainingSessionBean = saveTrainingSessionRequestJson.getAsTrainingSessionBean();
		
			try{											
				fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
				log.info("User - " + userid + " has successfully saved a trainingSession with id " + fitnessTrainingSessionBean.getTrainingSessionId());							
				/*prepare response json*/
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
				/*set response json*/
				gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase() ,saveFitnessTrainingSessionResponseJson);
			
			}catch(InvalidTimeDistributionException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TIME_DISTRIBUTION.toString());
				log.info("User - " + userid + " has failed to save a trainingSession due to InvalidTimeDistributionException");
			}catch(InvalidSpeedDistributionException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_SPEED_DISTRIBUTION.toString());
				log.info("User - " + userid + " has failed to save a trainingSession due to InvalidSpeedDistributionException");				
			}catch(TrainingSessionException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TRAINING_SESSION.toString());
				log.info("User - " + userid + " has failed to save a trainingSession due to TrainingSessionException");				
			}catch(InvalidTimestampException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_TIMESTAMP.toString());
				log.info("User - " + userid + " has failed to save a trainingSession due to InvalidTimestampException");				
			}catch(InvalidTimestampInChronologyException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_IN_CHRONOLOGY.toString());
				log.info("User - " + userid + " has failed to save a trainingSession due to InvalidTimestampInChronologyException");				
			}catch(TimeException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_TIME.toString());
				log.info("User - " + userid + " has failed to save a trainingSession due to TimeException");				
			}
		} 
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		
		try {
			return Formatter.getAsJson(gro, false);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}

	@GET
	@Path("id/{userid}/heartrateTest/resting/inTimeInterval")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getRestingHeartrateTestsInInterval(@PathParam("userid") String userid, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {

		    try{
	        	List<FitnessHeartrateTestBean> heartrateTests = fitnessManager.getFitnessHeartrateTestsByTypeInTimeInterval(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING, startTimeStamp, endTimeStamp);
			    HeartrateTestByRangeResponseJson heartrateTestsJson = new HeartrateTestByRangeResponseJson();
			    heartrateTestsJson.setUserId(userid);
			    heartrateTestsJson.setHeartrateTests(heartrateTests);	
			    gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), heartrateTestsJson);
	        }catch(InvalidStartTimestampException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_START_TIMESTAMP.toString());
	        	log.info("User - " + userid + " has failed to get resting heartrate tests due to InvalidStartTimestampException");				
	        }catch(InvalidEndTimestampException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_END_TIMESTAMP.toString());
	        	log.info("User - " + userid + " has failed to get resting heartrate tests due to InvalidEndTimestampException");				
	        }catch(InvalidDurationException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_DURATION.toString());
	        	log.info("User - " + userid + " has failed to get resting heartrate tests due to InvalidDurationException");
	        }catch(TimeException e){
		    	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_TIME.toString());
	        	log.info("User - " + userid + " has failed to get resting heartrate tests due to TimeException");				
	        }
		}else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}		
	}

	@GET
	@Path("id/{userid}/heartrateTest/maximal/inTimeInterval")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getMaximalHeartrateTestsInInterval(@PathParam("userid") String userid, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {
 
		   try{
				List<FitnessHeartrateTestBean> heartrateTests = fitnessManager.getFitnessHeartrateTestsByTypeInTimeInterval(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL, startTimeStamp, endTimeStamp);
			    HeartrateTestByRangeResponseJson heartrateTestsJson = new HeartrateTestByRangeResponseJson();
			    heartrateTestsJson.setUserId(userid);
			    heartrateTestsJson.setHeartrateTests(heartrateTests);
			    gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), heartrateTestsJson);
		   }catch(InvalidStartTimestampException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_START_TIMESTAMP.toString());
	        	log.info("User - " + userid + " has failed to get maximal heartrate tests due to InvalidStartTimestampException");				
	        }catch(InvalidEndTimestampException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_END_TIMESTAMP.toString());
	        	log.info("User - " + userid + " has failed to get maximal heartrate tests due to InvalidEndTimestampException");				
	        }catch(InvalidDurationException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_DURATION.toString());
	        	log.info("User - " + userid + " has failed to get maximal heartrate tests due to InvalidDurationException");
	        }catch(TimeException e){
		    	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_TIME.toString());
	        	log.info("User - " + userid + " has failed to get maximal heartrate tests due to TimeException");				
	        }
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}		
	}
	
	@GET
	@Path("id/{userid}/heartrateTest/threshold/inTimeInterval")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getThresholdHeartrateTestsInInterval(@PathParam("userid") String userid, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {
			
           try{ 
				List<FitnessHeartrateTestBean> heartrateTests = fitnessManager.getFitnessHeartrateTestsByTypeInTimeInterval(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD, startTimeStamp, endTimeStamp);
			    HeartrateTestByRangeResponseJson heartrateTestsJson = new HeartrateTestByRangeResponseJson();
			    heartrateTestsJson.setUserId(userid);
			    heartrateTestsJson.setHeartrateTests(heartrateTests);
			    gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), heartrateTestsJson);
            }catch(InvalidStartTimestampException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_START_TIMESTAMP.toString());
	        	log.info("User - " + userid + " has failed to get threshold heartrate tests due to InvalidStartTimestampException");				
	        }catch(InvalidEndTimestampException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_END_TIMESTAMP.toString());
	        	log.info("User - " + userid + " has failed to get threshold heartrate tests due to InvalidEndTimestampException");				
	        }catch(InvalidDurationException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_DURATION.toString());
	        	log.info("User - " + userid + " has failed to get threshold heartrate tests due to InvalidDurationException");
	        }catch(TimeException e){
		    	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_TIME.toString());
	        	log.info("User - " + userid + " has failed to get threshold heartrate tests due to TimeException");				
	        }
		} 
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}
		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}		
	}
	
	@GET
	@Path("id/{userid}/heartrateTest/orthostatic/inTimeInterval")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public String getOrthostaticHeartrateTestsInInterval(@PathParam("userid") String userid, @QueryParam("startTimeStamp") Timestamp startTimeStamp, @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {
           
			try{
				List<FitnessHeartrateTestBean> heartrateTests = fitnessManager.getFitnessHeartrateTestsByTypeInTimeInterval(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC, startTimeStamp, endTimeStamp);
			    HeartrateTestByRangeResponseJson heartrateTestsJson = new HeartrateTestByRangeResponseJson();
			    heartrateTestsJson.setUserId(userid);
			    heartrateTestsJson.setHeartrateTests(heartrateTests);			    
				gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), heartrateTestsJson);
   		   }catch(InvalidStartTimestampException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_START_TIMESTAMP.toString());
	        	log.info("User - " + userid + " has failed to get orthostatic heartrate tests due to InvalidStartTimestampException");				
	        }catch(InvalidEndTimestampException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_END_TIMESTAMP.toString());
	        	log.info("User - " + userid + " has failed to get orthostatic heartrate tests due to InvalidEndTimestampException");				
	        }catch(InvalidDurationException e){
	        	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_DURATION.toString());
	        	log.info("User - " + userid + " has failed to get orthostatic heartrate tests due to InvalidDurationException");
	        }catch(TimeException e){
		    	gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_TIME.toString());
	        	log.info("User - " + userid + " has failed to get orthostatic heartrate tests due to TimeException");				
	        }
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}

		try {
			return Formatter.getAsJson(gro, true);
		} catch (Exception ex) {
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
		
		GoodResponseObject gro = null;
		
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		
		if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {	
			try{				
				fitnessManager.saveHeartrateTest(heartrateTestBean);
				log.info("User - " + userid + " has successfully saved a heartrateTest with id " + heartrateTestBean.getHeartrateTestId());
			
				/*prepare response json*/
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
		
				gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(),saveHeartrateTestResponseJson);
			
			}catch(InvalidTimestampException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_TIMESTAMP.toString());
				log.info("User - " + userid + " has failed to save a heartrate test due to InvalidTimestampException");
			}catch(InvalidTimestampInChronologyException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_IN_CHRONOLOGY.toString());
				log.info("User - " + userid + " has failed to save a heartrate test due to InvalidTimestampInChronologyException");
			}catch(TimeException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TimeErrors.INVALID_TIME.toString());
				log.info("User - " + userid + " has failed to save a heartrate test due to TimeException");
			}catch(InvalidHeartrateException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), HeartrateTestErrors.INVALID_HEARTRATE.toString());
				log.info("User - " + userid + " has failed to save a heartrate test due to InvalidHeartrateException");
			}catch(HeartrateTestException e){
				gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), HeartrateTestErrors.INVALID_HEARTRATE.toString());
				log.info("User - " + userid + " has failed to save a heartrate test due to HeartrateTestException");
			}
		}
		else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		}

		try
		{
			return Formatter.getAsJson(gro, false);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}

	@GET
	@Path("id/{userid}/recoveryTime")
	@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecoveryTime(@PathParam("userid") String userid,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
					
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);

		GoodResponseObject gro = null;
	    if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {	

	    try{
	    	Timestamp recoveryTime = fitnessManager.getRecoveryTime(userid);
			  RecoveryTimeResponseJson recoveryTimeResponseJson = new RecoveryTimeResponseJson();
			  recoveryTimeResponseJson.setUserId(userid);
			  recoveryTimeResponseJson.setRecoveryTime(recoveryTime);
			 
			  String recentTrainingSessionId = fitnessManager.getRecentTrainingSessionId(userid);
			  if(null != recentTrainingSessionId ){
				  recoveryTimeResponseJson.setRecentTrainingSessionId(recentTrainingSessionId);
			  }
			  FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessManager.getHomeostasisIndexModelForUser(userid);
			  if(null != fitnessHomeostasisIndexBean){
			      recoveryTimeResponseJson.setLocalRegressionMinimumOfHomeostasisIndex(fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
			      recoveryTimeResponseJson.setRecentMinimumOfHomeostasisIndex(fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());	          
			      recoveryTimeResponseJson.setTraineeClassification(fitnessHomeostasisIndexBean.getTraineeClassification());
			  }
				
			  gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), recoveryTimeResponseJson);
	     }catch(AbsenceOfTrainingSessionException e){
	    	 gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.ABSENCE_OF_TRAINING_SESSION.toString());
			 log.info("User - " + userid + " has failed to get recovery time due to AbsenceOfTrainingSessionException");
	     }catch(AbsenceOfHomeostasisIndexModelException e){
	    	 gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), HomeostasisModelErrors.ABSENCE_OF_HOMEOSTASIS_INDEX_MODEL.toString());
			 log.info("User - " + userid + " has failed to get recovery time due to AbsenceOfHomeostasisIndexsModelException");
	     }catch(TrainingSessionException e){
	    	 gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), TrainingSessionErrors.INVALID_TRAINING_SESSION.toString());
			 log.info("User - " + userid + " has failed to get recovery time due to TrainingSessionException");
	     }catch(HomeostasisModelException e){
	    	 gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), HomeostasisModelErrors.HOMEOSTASIS_INDEX_MODEL_ERROR.toString());
			 log.info("User - " + userid + " has failed to get recovery time due to AbsenceOfTrainingSessionException");
	     }
	   }else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
	   }  

		try
		{
			return Formatter.getAsJson(gro, false);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}
	
	
	@GET
	@Path("id/{userid}/homeostasisIndex")
	@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String getHomeostasisIndex(@PathParam("userid") String userid,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType){
			
		AuthenticationStatus authStatus = userManager.authenticateRequest(accessToken, accessTokenType);
		GoodResponseObject gro = null;
	    if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)) {	
	      /* Get Homeostasis Index*/
	     try{
		  Double homeostasisIndex = fitnessManager.getHomeostasisIndex(userid);
		  HomeostasisIndexResponseJson  homeostasisIndexResponseJson = new  HomeostasisIndexResponseJson();
		  homeostasisIndexResponseJson.setUserid(userid);
		  homeostasisIndexResponseJson.setHomeostasisIndex(homeostasisIndex);
		  gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), homeostasisIndexResponseJson);
	     }catch(AbsenceOfHomeostasisIndexModelException e){
	    	 gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), HomeostasisModelErrors.ABSENCE_OF_HOMEOSTASIS_INDEX_MODEL.toString());
			 log.info("User - " + userid + " has failed to get homeostasis index model due to AbsenceOfHomeostasisIndexsModelException");
	     }catch(HomeostasisModelException e){
	    	 gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), HomeostasisModelErrors.HOMEOSTASIS_INDEX_MODEL_ERROR.toString());
			 log.info("User - " + userid + " has failed to get homeostasis index model due to AbsenceOfHomeostasisIndexsModelException");
	     }
	     }else {
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), authStatus.getAuthenticationStatus().toString());
			log.info("User - " + userid + " " + authStatus.getAuthenticationStatus().toString());
		 }  

		try
		{
			return Formatter.getAsJson(gro, false);
		}
		catch(Exception ex)
		{
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(ex).build());
		}
	}
	
	
	@GET
	@Path("/all")
	@Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public String getTraineeIds(@QueryParam("userid") String userID,@QueryParam("accessToken") String accessToken, @QueryParam("accessTokenType") String accessTokenType) {
		
		List<UserIdBean> traineeIds = userManager.getUserIds();
		GoodResponseObject gro = null;
		
		if(null != traineeIds){
			TraineeIdsResponseJson traineeIdsResponseJson = new TraineeIdsResponseJson();
			traineeIdsResponseJson.setTraineeIds(traineeIds);
			gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(),traineeIdsResponseJson);
		}else{
			gro = new GoodResponseObject(Status.NOT_ACCEPTABLE.getStatusCode(), UserErrors.USERS_NOT_PRESENT.toString());	
		}
		
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
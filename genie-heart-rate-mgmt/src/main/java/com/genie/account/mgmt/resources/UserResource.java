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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.core.UserManager;
import com.genie.account.mgmt.json.user.UserInfoJSON;
import com.genie.account.mgmt.util.AuthenticationStatus;
import com.genie.account.mgmt.util.RegisterRequestJSON;
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
		User user = userManager.getUserInformationByEmail(email);
		UserInfoJSON userInfoJSON = new UserInfoJSON();
		userInfoJSON.copyFromUserBean(user);
		GoodResponseObject gro = new GoodResponseObject(Status.OK.getStatusCode(), Status.OK.getReasonPhrase(), userInfoJSON);
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
}

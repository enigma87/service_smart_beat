package com.genie.account.mgmt.json.facebook;

/**
 * @author vidhun
 *
 */
public class GraphAPIResponseJSON {

	private String id;
	private String name;
	private String email;
	private String firstName;
	private String lastName;
	private GraphAPIErrorJSON error;
	
    public void setId(String id) {
	this.id = id;
    }
    
    public String getId() {
		return id;
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
    public String getName() {
		return name;
	}
    
    public void setEmail(String email) {
		this.email = email;
	}
    
    public String getEmail() {
		return email;
	}
    
    public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
    
    public String getFirstName() {
		return firstName;
	}
    
    public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    
    public String getLastName() {
		return lastName;
	}
    
    public GraphAPIErrorJSON getError() {
		return error;
	}
    
    public void setError(GraphAPIErrorJSON error) {
		this.error = error;
	}
}

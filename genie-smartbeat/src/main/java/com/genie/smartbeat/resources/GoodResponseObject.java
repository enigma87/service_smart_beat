/**
 * 
 */
package com.genie.smartbeat.resources;

/**
 * @author vidhun
 *
 */
public class GoodResponseObject 
{
	public int status;
	public String message;
	public Object obj;
	
	public GoodResponseObject(int status, String message)
	{
		this.status = status;
		this.message = message;
	}
	
	public GoodResponseObject(int status, String message, Object obj)
	{
		this.status = status;
		this.message = message;
		this.obj = obj;
	}
	
	public int getStatus()
	{
		return this.status;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public Object getObj()
	{
		return this.obj;
	}
	
	public void setObj(Object obj)
	{
		this.obj = obj;
	}
}

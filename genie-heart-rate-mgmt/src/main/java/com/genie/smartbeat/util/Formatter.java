/**
 * 
 */
package com.genie.smartbeat.util;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author dhasarathy
 *
 */
public class Formatter 
{
	
	public static String getAsJson(Object pojoObject, boolean includeNull) throws Exception
	{
    	ObjectMapper mapper = new ObjectMapper();
    	if (!includeNull) 
    		mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    	
    	mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    	return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojoObject);
	}

}
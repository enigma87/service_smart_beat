/**
 * 
 */
package com.genie.smartbeat;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author manojkumar
 *
 */
public class TestSetup 
{

	private static AbstractApplicationContext applicationContext = null;
	
	public static AbstractApplicationContext getInstance()
	{
		if (applicationContext == null)
		{System.out.println("Application context is initialised");
			 applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
			 applicationContext.registerShutdownHook();
		}
		return applicationContext;
	}
}

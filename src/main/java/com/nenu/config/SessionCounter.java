package com.nenu.config;

import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Configuration;

@Configuration
@WebListener
public class SessionCounter implements HttpSessionListener{
	
	 private static int activeSessions = 0;
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		activeSessions++;
		
		
		//System.out.println("在线人数"+activeSessions);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
	}
	//获取活动的session个数(在线人数)
    public static int getActiveSessions() {
        return activeSessions;
    }
    
}

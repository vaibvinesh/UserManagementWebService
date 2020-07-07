package com.springcourse.project.security;

import com.springcourse.project.AppContext;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public static final long PASSWORD_TOKEN_EXPIRATION = 1000*60*60*24; //1day 
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String PASSWORD_RESET_URL ="/users/password-reset-request" ;
    public static final String PASSWORD_RESET_FINAL = "/users/password-reset";
    public static final String H2_CONSOLE = "/h2-console/**";
    
    public static String getTokenSecret() {
    	SecurityProperties properties = (SecurityProperties) AppContext.getBean("securityProperties");
    	return properties.getTokenSecret();
    }
    
}

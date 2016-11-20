package com.anitech.tquesto.util;

/**
 * @author Tapas
 *
 */
public class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    
    // Spring profile for development and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    
    // Auth
    public static final String AUTH_SECURITY_CONTEXT = "/api/**";
    
}

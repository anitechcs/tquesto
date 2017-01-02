package com.anitech.tquesto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object to return as body in JWT Authentication.
 * 
 * @author Tapas
 * 
 */
public class JwtToken {

	private String authToken;

    public JwtToken(String authToken) {
        this.authToken = authToken;
    }

    @JsonProperty("auth_token")
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
}

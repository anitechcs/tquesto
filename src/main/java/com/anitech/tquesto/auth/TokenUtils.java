package com.anitech.tquesto.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT Token utility class
 * 
 * @author Tapas
 *
 */
@Component
public class TokenUtils {

	private final Logger LOG = LoggerFactory.getLogger(TokenUtils.class);
	
	@Value("${app.token.secret}")
	private String secret;
	
	@Value("${app.token.expiry}")
	private Long expiry;
	
	
	public String generateToken(UserDetails userDetails) {
		LOG.debug("Inside TokenUtils->generateToken() method!");
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", userDetails.getUsername());
		claims.put("created", getCurrentDate());
		return generateTokenFromClaims(claims);
	}
	
	public String getUserNameFromToken(String token) {
		LOG.debug("Inside TokenUtils->getUserNameFromToken() method!");
		String userName;
		try {
			Claims claims = getClaimsFromToken(token);
			userName = claims.getSubject();
		} catch (Exception e) {
			userName = null;
		}
		return userName;
	}
	
	public Claims getClaimsFromToken(String token) {
		LOG.debug("Inside TokenUtils->getClaimsFromToken() method!");
		Claims claims;
		try {
			claims = Jwts
					.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	
	public Date getCreatedDateFromToken(String token) {
		LOG.debug("Inside TokenUtils->getCreatedDateFromToken() method!");
		Date created;
		try {
			Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get("created"));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}
	
	public Date getExpiryDateFromToken(String token) {
		LOG.debug("Inside TokenUtils->getExpiryDateFromToken() method!");
		Date expiration;
		try {
			Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		LOG.debug("Inside TokenUtils->validateToken() method!");
		String userName = getUserNameFromToken(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String generateTokenFromClaims(Map<String, Object> claims) {
		LOG.debug("Inside TokenUtils->generateTokenFromClaims() method!");
		return Jwts
				.builder()
				.setClaims(claims)
				.setExpiration(getExpiryDate())
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	private Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}
	
	private Date getExpiryDate() {
		return new Date(System.currentTimeMillis() + expiry * 1000);
	}
	
	private boolean isTokenExpired(String token) {
		Date expiration = getExpiryDateFromToken(token);
		return expiration.before(getCurrentDate());
	}
	
}

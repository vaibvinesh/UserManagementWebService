package com.springcourse.project.ui.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.springcourse.project.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {
	private final Random rand = new SecureRandom();
	private final String charSpace = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private final int iterations = 10000;
	private final int key_length = 256;
	
	public String generateUserID(int length) {
		return randomString(length);
	}
	
	public String generateAddressID(int length) {
		return randomString(length);
	}
	private String randomString(int length) {
		StringBuilder st = new StringBuilder(length);
		
		for(int i =0; i<length; i++) {
			st.append(charSpace.charAt(rand.nextInt(charSpace.length())));
		}
		return st.toString();
		
	}
	
	public boolean hasTokenExpired(String token) {
		
		boolean returnVal = false;
		
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(SecurityConstants.getTokenSecret())
					.parseClaimsJws(token).getBody();
			Date tokenExpirationDate = claims.getExpiration();
			Date date = new Date();
			returnVal = tokenExpirationDate.before(date);
			
		} catch (ExpiredJwtException e) {
			returnVal = true;
		}
		
		return returnVal;
		
	}
	
	public String getEmailVerificationToken(String userId) {
		
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret() )
				.compact();
		
		return token;
	}
	
	public String getPasswordResetToken(String userId) {
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_TOKEN_EXPIRATION))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret() )
				.compact();
		
		return token;
	}
	
}

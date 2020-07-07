package com.springcourse.project.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.springcourse.project.io.entity.UserEntity;
import com.springcourse.project.io.repositories.UserRepository;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter{

	UserRepository userRepo;
	public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository repo) {
		super(authenticationManager);
		this.userRepo = repo;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException{
		
		String header = req.getHeader(SecurityConstants.HEADER_STRING);
		if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		
		String token = req.getHeader(SecurityConstants.HEADER_STRING);
		
		if(token != null) {
			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
			
			String user = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret())
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			//System.out.println(user);
			if(user != null) {
				UserEntity entity = userRepo.findByEmail(user);
				if(entity==null) return null;
				UserPrincipal principal = new UserPrincipal();
				principal.setEntity(entity);
				principal.setUserId(entity.getUserId());
				return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
			}
			return null;
		}
		
			
		return null;
	}
	
	
	
	
	
}

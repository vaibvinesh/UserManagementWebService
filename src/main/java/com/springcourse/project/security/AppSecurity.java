package com.springcourse.project.security;

import java.util.Arrays;

import javax.annotation.security.PermitAll;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.springcourse.project.io.repositories.UserRepository;
import com.springcourse.project.service.UserService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class AppSecurity extends WebSecurityConfigurerAdapter {
	
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepo;
	
	public AppSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository repo) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepo = repo;
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .cors().and()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
        .permitAll()
        .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
        .permitAll()
        .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
        .permitAll()
        .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_FINAL)
        .permitAll()
        .antMatchers(SecurityConstants.H2_CONSOLE)
        .permitAll()
        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**" )
        .permitAll()
        //.antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("DELETE_AUTHORITY")
        .anyRequest().authenticated().and().addFilter(getAuthFilter())
        .addFilter(new AuthorizationFilter(authenticationManager(), this.userRepo))
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        //http.headers().frameOptions().disable(); //only for H2, disable while putting on git
    }
	
	 @Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	    }
	 
	 public AuthFilter getAuthFilter() throws Exception{
		 final AuthFilter authFilter = new AuthFilter(authenticationManager());
		 authFilter.setFilterProcessesUrl("/users/login");
		 return authFilter;
		 
	 }
	 
	 public CorsConfigurationSource corsConfigurationSource() {
		 
		 final CorsConfiguration configuration = new CorsConfiguration();
		 
		 configuration.setAllowedOrigins(Arrays.asList("*"));
		 configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		 configuration.setAllowCredentials(true);
		 configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		 
		 final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		 source.registerCorsConfiguration("/**", configuration);
		 return source;
		 
		 
	 }
}

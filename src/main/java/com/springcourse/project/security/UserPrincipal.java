package com.springcourse.project.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springcourse.project.io.entity.AuthorityEntity;
import com.springcourse.project.io.entity.RoleEntity;
import com.springcourse.project.io.entity.UserEntity;

public class UserPrincipal implements UserDetails {

	private UserEntity entity;
	private String userId;
	private static final long serialVersionUID = -8980088354464475541L;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> auth = new ArrayList<>();
		List<AuthorityEntity> roleAuth= new ArrayList<>();
		//get roles
		
		List<RoleEntity> roles=  this.entity.getRoles();
		roles.forEach((role) ->{
			auth.add(new SimpleGrantedAuthority(role.getName()));
			roleAuth.addAll(role.getAuthorities());
		});
		roleAuth.forEach((authority)->{
			auth.add(new SimpleGrantedAuthority(authority.getName()));
		});
		return auth;
	}

	@Override
	public String getPassword() {
		return this.entity.getEncryptedPassword();
	}

	@Override
	public String getUsername() {
		return this.entity.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.entity.isEmailVerificationStatus();
	}

	public UserEntity getEntity() {
		return entity;
	}

	public void setEntity(UserEntity entity) {
		this.entity = entity;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}

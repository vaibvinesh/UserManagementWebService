package com.springcourse.project;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.springcourse.project.io.entity.AuthorityEntity;
import com.springcourse.project.io.entity.RoleEntity;
import com.springcourse.project.io.entity.UserEntity;
import com.springcourse.project.io.repositories.AuthorityRepository;
import com.springcourse.project.io.repositories.RoleRepository;
import com.springcourse.project.io.repositories.UserRepository;

@Component
public class InititalUsersSetup {

	@Autowired
	AuthorityRepository repo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@EventListener
	@Transactional
	public void onApplicationStartup(ApplicationReadyEvent e) {
		
		AuthorityEntity READ = createAuthority("READ_AUTHORITY");
		AuthorityEntity WRITE = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity DELETE = createAuthority("DELETE_AUTHORITY");
		
		RoleEntity userRole = createRole("ROLE_USER", Arrays.asList(READ, WRITE));
		RoleEntity adminRole = createRole("ROLE_ADMIN", Arrays.asList(READ,WRITE,DELETE));
		if(adminRole == null) return;
		UserEntity adminUser = userRepo.findByEmail("vaibvinesh@gmail.com");
		System.out.println(adminUser.getRoles().size());
		if(adminUser != null && adminUser.getRoles().size() == 0) {
			adminUser.setRoles(Arrays.asList(adminRole));
			userRepo.save(adminUser);
		}
		
	}
	
	@Transactional
	private AuthorityEntity createAuthority(String name) {
		AuthorityEntity auth = repo.findByName(name);
		if(auth== null) {
			auth = new AuthorityEntity();
			auth.setName(name);
			repo.save(auth);
		}
		return auth;
	}
	
	@Transactional
	private RoleEntity createRole(String name, List<AuthorityEntity> authorities) {
		
		RoleEntity role = roleRepo.findByName(name);
		
		if(role==null) {
			role = new RoleEntity();
			role.setAuthorities(authorities);
			role.setName(name);
			roleRepo.save(role);
		}
		
		return role;
	}
}

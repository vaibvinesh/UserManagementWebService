package com.springcourse.project.service.impl;

import java.util.ArrayList;
import java.util.List;



import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.springcourse.project.exeptions.UserServiceException;
import com.springcourse.project.io.entity.PasswordResetEntity;
import com.springcourse.project.io.entity.RoleEntity;
import com.springcourse.project.io.entity.UserEntity;
import com.springcourse.project.io.repositories.PasswordResetRepo;
import com.springcourse.project.io.repositories.RoleRepository;
import com.springcourse.project.io.repositories.UserRepository;
import com.springcourse.project.security.UserPrincipal;
import com.springcourse.project.service.UserService;
import com.springcourse.project.ui.model.response.ErrorMessages;
import com.springcourse.project.ui.shared.AmazonSES;
import com.springcourse.project.ui.shared.Utils;
import com.springcourse.project.ui.shared.dto.AddressDTO;
import com.springcourse.project.ui.shared.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	AmazonSES emailClient;
	
	@Autowired
	PasswordResetRepo passwordResetTokenRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Override
	public UserDTO createUser(UserDTO user) {
		
		if(userRepo.findByEmail(user.getEmail()) != null) throw new UserServiceException("Record already Exists");
		
		//UserEntity userEntity = new UserEntity();
		//BeanUtils.copyProperties(user, userEntity);
		String userId = utils.generateUserID(30);
		user.setUserId(userId);
		ModelMapper modelMapper = new ModelMapper();
		for(AddressDTO add : user.getAddresses()) {
			add.setAddressId(utils.generateAddressID(30));
			add.setUserDetails(user);
		}
		
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setEmailVerificationToken(utils.getEmailVerificationToken(userId));
		
		List<RoleEntity> roleEntities = new ArrayList<>();
		for(String role : user.getRoles()) {
			RoleEntity roleEntity = roleRepo.findByName(role);
			roleEntities.add(roleEntity);
		}
		userEntity.setRoles(roleEntities);
		UserEntity storedDetails = userRepo.save(userEntity);
		UserDTO returnVal = modelMapper.map(storedDetails, UserDTO.class);
		//BeanUtils.copyProperties(storedDetails, returnVal);
		emailClient.verifyEmail(returnVal);
		return returnVal;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity entity = userRepo.findByEmail(email);
		if(entity == null) throw new UsernameNotFoundException(email);
		UserPrincipal returnVal = new UserPrincipal();
		returnVal.setEntity(entity);
		return returnVal;
	}

	@Override
	public UserDTO getUser(String email) {
		UserEntity entity = userRepo.findByEmail(email);
		if(entity == null) throw new UsernameNotFoundException(email);
		ModelMapper modelMapper = new ModelMapper();
		UserDTO returnVal = modelMapper.map(entity, UserDTO.class);
		//UserDTO returnVal = new UserDTO();
		//BeanUtils.copyProperties(entity, returnVal);
		return returnVal;
	}

	@Override
	public UserDTO getUserByUserId(String id) {
		//UserDTO returnVal = new UserDTO();
		UserEntity entity = userRepo.findByUserId(id);
		if(entity == null ) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		ModelMapper modelMapper = new ModelMapper();
		UserDTO returnVal = modelMapper.map(entity, UserDTO.class);
		//BeanUtils.copyProperties(entity, returnVal);
		return returnVal;
	}

	@Override
	public UserDTO updateUser(UserDTO user, String id) {
		UserEntity entity = userRepo.findByUserId(id);
		if(entity == null) 
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		entity.setFirstName(user.getFirstName());
		entity.setLastName(user.getLastName());
		UserEntity updatedDetails = userRepo.save(entity);
		ModelMapper modelMapper = new ModelMapper();
		UserDTO returnVal = modelMapper.map(updatedDetails, UserDTO.class);
		//BeanUtils.copyProperties(updatedDetails, returnVal);
		return returnVal;
	}

	@Override
	public void deleteUserById(String id) {
		UserEntity entity = userRepo.findByUserId(id);
		if(entity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userRepo.delete(entity);
	}

	@Override
	public List<UserDTO> getUsers(int page, int limit) {
		List<UserDTO> returnVal = new ArrayList<>();
		Pageable request= PageRequest.of(page, limit);
		Page<UserEntity> respPage = userRepo.findAll(request);
		List<UserEntity> users = respPage.getContent();
		ModelMapper modelMapper = new ModelMapper();
		for(UserEntity obj : users) {
			UserDTO details = modelMapper.map(obj, UserDTO.class);
			//BeanUtils.copyProperties(obj, details);
			returnVal.add(details);
		}
		return returnVal;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		UserEntity entity = userRepo.findByEmailVerificationToken(token);
		boolean returnVal = false;
		if(entity !=null) {
			boolean hasExpired = utils.hasTokenExpired(token);
			if(!hasExpired) {
				entity.setEmailVerificationToken(null);
				entity.setEmailVerificationStatus(Boolean.TRUE);
				userRepo.save(entity);
				returnVal = true;
			}
		}
		return returnVal;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		boolean returnVal  = false;
		
		UserEntity entity = userRepo.findByEmail(email);
		if(entity==null) {
			return returnVal;
		}
		String token = utils.getPasswordResetToken(entity.getUserId());
		PasswordResetEntity passwordEntity = new PasswordResetEntity();
		passwordEntity.setToken(token);
		passwordEntity.setUserDetails(entity);
		passwordResetTokenRepo.save(passwordEntity);
		returnVal = emailClient.sendPasswordResetEmail(entity.getFirstName(), entity.getEmail(), token);
		return returnVal;
	}

	@Override
	public boolean resetPassword(String token, String password) {
		boolean returnVal = false;
		
		if(utils.hasTokenExpired(token)) {
			PasswordResetEntity oldEntity = passwordResetTokenRepo.findByToken(token);
			if(oldEntity != null)
				passwordResetTokenRepo.delete(oldEntity);
			return returnVal;
		}
			
		
		PasswordResetEntity entity  = passwordResetTokenRepo.findByToken(token);
		if(entity == null)
			return returnVal;
		
		String encoded = bCryptPasswordEncoder.encode(password);
		UserEntity details = entity.getUserDetails();
		details.setEncryptedPassword(encoded);
		UserEntity updated = userRepo.save(details);
		
		if(updated != null && updated.getEncryptedPassword().equalsIgnoreCase(encoded))
			returnVal = true;
		
		return returnVal;
	}
	
}

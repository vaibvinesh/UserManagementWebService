package com.springcourse.project.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.springcourse.project.ui.shared.dto.UserDTO;

public interface UserService extends UserDetailsService {
	UserDTO createUser(UserDTO user);
	UserDTO getUser(String email);
	UserDTO getUserByUserId(String id);
	UserDTO updateUser(UserDTO user, String id);
	void deleteUserById(String id);
	List<UserDTO> getUsers(int page, int limit);
	boolean verifyEmailToken(String token);
	boolean requestPasswordReset(String email);
	boolean resetPassword(String token, String password);
}

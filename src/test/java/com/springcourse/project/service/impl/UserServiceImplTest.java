package com.springcourse.project.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springcourse.project.exeptions.UserServiceException;
import com.springcourse.project.io.entity.AddressEntity;
import com.springcourse.project.io.entity.UserEntity;
import com.springcourse.project.io.repositories.UserRepository;
import com.springcourse.project.ui.model.response.AddressRespUI;
import com.springcourse.project.ui.shared.AmazonSES;
import com.springcourse.project.ui.shared.Utils;
import com.springcourse.project.ui.shared.dto.AddressDTO;
import com.springcourse.project.ui.shared.dto.UserDTO;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl service;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	AmazonSES emailClient;
	
	
	String userId = "asd1234asfwf2";
	String password = "sad34jmolij9323as";
	UserEntity entity;
	UserDTO dto;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		entity = new UserEntity();
		entity.setId(2L);
		entity.setEncryptedPassword(password);
		entity.setUserId(userId);
		entity.setFirstName("Trash");
		entity.setLastName("Garbage");
		entity.setEmail("trash@trash.com");
		entity.setEmailVerificationToken("asdkg128ujalsihf0qasf0321r-1w");
		entity.setAddresses(getAddressEntity());
		
		dto =  new UserDTO();
		dto.setAddresses(getAddressDTO());
		dto.setFirstName("Trash");
		dto.setLastName("Garbage");
		dto.setPassword("dump");
		dto.setEmail("trash@trash.com");
	}

	@Test
	final void testGetUser() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(entity);
		
		//if(service == null) System.out.println("service is null");
		
		UserDTO testDTO = service.getUser("test@test.com");
		assertNotNull(testDTO);
		assertEquals("Trash", testDTO.getFirstName());
	}
	
	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, 
				() -> {
					service.getUser("trash@trash.com");
				}
				);
	}
	
	@Test
	final void testCreateUser_UserServiceException() {
		when(userRepository.findByEmail(anyString())).thenReturn(entity);
		
		assertThrows(UserServiceException.class, 
				() -> {
					service.createUser(dto);
				}
				);
	}
	
	@Test
	final void testCreateUser() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressID(anyInt())).thenReturn("hhjh23423hj2");
		when(utils.generateUserID(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(password);
		when(userRepository.save(any(UserEntity.class))).thenReturn(entity);
		Mockito.doNothing().when(emailClient).verifyEmail(any(UserDTO.class));
		
		UserDTO stored = service.createUser(dto);
		assertNotNull(stored);
		assertEquals(entity.getFirstName(), stored.getFirstName());
		assertNotNull(stored.getUserId());
		assertEquals(entity.getUserId(), stored.getUserId());
		assertEquals(entity.getAddresses().size(), stored.getAddresses().size());
		
		verify(utils, times(stored.getAddresses().size())).generateAddressID(anyInt());
		verify(utils, times(1)).generateUserID(30);
		verify(bCryptPasswordEncoder, times(1)).encode(anyString());
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}
	
	private List<AddressDTO> getAddressDTO(){
		AddressDTO address = new AddressDTO();
		address.setType("Shipping");
		address.setBuilding("G-1902");
		address.setStreetName("200 ft road");
		address.setCity("San Fransisco");
		address.setCountry("China");
		address.setPostalCode("341351");
		address.setState("Manchester");
		
		AddressDTO address1 = new AddressDTO();
		address.setType("Billing");
		address.setBuilding("G-1902");
		address.setStreetName("200 ft road");
		address.setCity("San Fransisco");
		address.setCountry("China");
		address.setPostalCode("341351");
		address.setState("Manchester");
		
		List<AddressDTO> list = new ArrayList<>();
		list.add(address);
		list.add(address1);
		
		return list;
	}
	
	private List<AddressEntity> getAddressEntity(){
		List<AddressDTO> list = getAddressDTO();
		ModelMapper modelMapper = new ModelMapper();
		java.lang.reflect.Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		List<AddressEntity> returnVal = modelMapper.map(list, listType);
		return returnVal;
	}
	
}

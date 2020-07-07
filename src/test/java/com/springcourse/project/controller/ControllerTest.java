package com.springcourse.project.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.springcourse.project.service.UserService;
import com.springcourse.project.ui.model.response.UserRespUI;
import com.springcourse.project.ui.shared.dto.AddressDTO;
import com.springcourse.project.ui.shared.dto.UserDTO;

class ControllerTest {
	
	@InjectMocks
	Controller controller;
	
	@Mock
	UserService userService;
	
	UserDTO dto;
	
	String userId = "asd1234asfwf2";
	String password = "sad34jmolij9323as";
	String emailVerificationToken= "asdkg128ujalsihf0qasf0321r-1w";
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		dto = new UserDTO();
		dto.setAddresses(getAddressDTO());
		dto.setFirstName("Trash");
		dto.setLastName("Garbage");
		dto.setPassword("dump");
		dto.setEmail("trash@trash.com");
		dto.setEncryptedPassword(password);
		dto.setUserId(userId);
		dto.setEmailVerificationToken(emailVerificationToken);
		dto.setEmailVerificationStatus(Boolean.FALSE);
	}

	@Test
	final void testGetUser() {
		when(userService.getUserByUserId(anyString())).thenReturn(dto);
		
		UserRespUI details = controller.getUser("trash@trash.com");
		
		assertNotNull(details);
		assertNotNull(details.getAddresses());
		assertEquals(details.getFirstName(), dto.getFirstName());
		assertEquals(details.getLastName(), dto.getLastName());
		assertEquals(details.getUserId(), dto.getUserId());
		assertEquals(details.getAddresses().size(), dto.getAddresses().size());
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
}

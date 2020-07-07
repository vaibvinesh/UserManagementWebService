package com.springcourse.project.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springcourse.project.exeptions.UserServiceException;
import com.springcourse.project.service.AddressService;
import com.springcourse.project.service.UserService;
import com.springcourse.project.ui.model.request.PasswordResetModel;
import com.springcourse.project.ui.model.request.PasswordResetRequestModel;
import com.springcourse.project.ui.model.request.UserDetailsRequestModel;
import com.springcourse.project.ui.model.response.AddressRespUI;
import com.springcourse.project.ui.model.response.ErrorMessages;
import com.springcourse.project.ui.model.response.OperationResponse;
import com.springcourse.project.ui.model.response.OperationStatus;
import com.springcourse.project.ui.model.response.Operations;
import com.springcourse.project.ui.model.response.UserRespUI;
import com.springcourse.project.ui.shared.Roles;
import com.springcourse.project.ui.shared.dto.AddressDTO;
import com.springcourse.project.ui.shared.dto.UserDTO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("users")
public class Controller {
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	UserService userService;
	
	@PostAuthorize("hasRole('ADMIN') or returnObject.userId == principal.userId")
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="${controller.authheader.desc}", paramType="header")
	})
	@ApiOperation(value="Get User details endpoint", 
			notes="Use this endpoint to fetch user details, provide userId in the url like /users/<userId>")
	@GetMapping(path="/{id}")
	public UserRespUI getUser(@PathVariable String id) {
		UserDTO details = userService.getUserByUserId(id);
		ModelMapper modelMapper = new ModelMapper();
		UserRespUI returnVal = modelMapper.map(details, UserRespUI.class);
		//BeanUtils.copyProperties(details, returnVal);
		return returnVal;
	}
	
	@ApiOperation(value="Create User details endpoint", 
			notes="Use this endpoint to create new user, provide userId and password in post body")
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public UserRespUI createUser(@RequestBody UserDetailsRequestModel details) throws Exception {
		
		if(details.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		//UserDTO userDTO = new UserDTO();
		//BeanUtils.copyProperties(details, userDTO);
		ModelMapper modelMapper = new ModelMapper();
		UserDTO userDTO  = modelMapper.map(details, UserDTO.class);
		userDTO.setRoles(Arrays.asList(Roles.ROLE_USER.name()));
		UserDTO createUser = userService.createUser(userDTO);
		UserRespUI returnVal = modelMapper.map(createUser, UserRespUI.class);
		//BeanUtils.copyProperties(createUser, returnVal);
		return returnVal;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="${controller.authheader.desc}", paramType="header")
	})
	@ApiOperation(value="Update User details endpoint", 
	notes="Use this endpoint to update user, provide details to be updated in put body")
	@PutMapping(path="/{id}", 
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public UserRespUI updateUser(@RequestBody UserDetailsRequestModel details, @PathVariable String id) {
		ModelMapper modelMapper = new ModelMapper();
		UserDTO userDTO = modelMapper.map(details, UserDTO.class);
		//BeanUtils.copyProperties(details, userDTO);
		
		UserDTO updatedUser = userService.updateUser(userDTO, id);
		UserRespUI returnVal = modelMapper.map(updatedUser, UserRespUI.class);
		//BeanUtils.copyProperties(updatedUser, returnVal);
		return returnVal;
	}
	
	@PreAuthorize("hasAuthority('DELETE_AUTHORITY') or #id == principal.userId")
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="${controller.authheader.desc}", paramType="header")
	})
	@ApiOperation(value="Delete User details endpoint", 
	notes="Use this endpoint to delete user, provide userId in url like /users/<userId>")
	@DeleteMapping(path="/{id}")
	public OperationStatus deleteUser(@PathVariable String id) {
		OperationStatus returnVal = new OperationStatus();
		returnVal.setOperationName(Operations.DELETE.name());
		returnVal.setOperationResult(OperationResponse.SUCCESS.name());
		userService.deleteUserById(id);
		return returnVal;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="${controller.authheader.desc}", paramType="header")
	})
	@ApiOperation(value="Get Users endpoint", 
	notes="Use this endpoint to get users on this platform")
	@GetMapping
	public List<UserRespUI> getUsers(@RequestParam(value = "page", defaultValue="0") int page, 
			@RequestParam(value="limit", defaultValue="25") int limit){
		List<UserRespUI> returnVal = new ArrayList<>();		
		List<UserDTO> users = userService.getUsers(page,limit);
		ModelMapper modelMapper = new ModelMapper();
		for(UserDTO obj : users) {
			UserRespUI userModel =modelMapper.map(obj, UserRespUI.class);
			returnVal.add(userModel);
		}
		return returnVal;
		
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="${controller.authheader.desc}", paramType="header")
	})
	@ApiOperation(value="Get User Address details endpoint", 
	notes="Use this endpoint to fetch user addresses, provide userId in url like /users/<userId>/addresses")
	@GetMapping(path="/{id}/addresses")
	public List<AddressRespUI> getUserAddresses(@PathVariable String id) {
		
		List<AddressDTO> details = addressService.getAddresses(id);
		ModelMapper modelMapper = new ModelMapper();
		java.lang.reflect.Type listType = new TypeToken<List<AddressRespUI>>() {}.getType();
		List<AddressRespUI> returnVal = modelMapper.map(details, listType);
		//BeanUtils.copyProperties(details, returnVal);
		return returnVal;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="Authorization", value="${controller.authheader.desc}", paramType="header")
	})
	@ApiOperation(value="Get User Address by id endpoint", 
	notes="Use this endpoint to fetch user address by addressId, provide userId and addressId in url like /users/<userId>/addresses/<addressId>")
	@GetMapping(path="/{userId}/addresses/{addressId}")
	public EntityModel<AddressRespUI> getUserAddress(@PathVariable String userId, @PathVariable String addressId ) {
		ModelMapper modelMapper = new ModelMapper();
		AddressDTO details = addressService.getAddress(addressId);
		AddressRespUI returnVal = modelMapper.map(details, AddressRespUI.class);
		List<Link> linkList = new ArrayList<>();
		linkList.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Controller.class).getUser(userId)).withRel("GetUserDetails"));
		linkList.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Controller.class).getUserAddresses(userId)).withRel("GetAllUserAddresses"));
		linkList.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Controller.class).getUserAddress(userId, addressId)).withSelfRel());
		//returnVal.add(linkList);
		return EntityModel.of(returnVal, linkList);
	}
	
	@ApiOperation(value="Verify Email Token", 
			notes="Use this endpoint to verify your email address, provide token as parameter like /users/email-verification?token=<tokenValue>")
	@GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public OperationStatus verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatus returnValue = new OperationStatus();
        returnValue.setOperationName(Operations.VERIFY_EMAIL.name());
        boolean isVerified = userService.verifyEmailToken(token);
        if(isVerified)
        	returnValue.setOperationResult(OperationResponse.SUCCESS.name());
        else
        	returnValue.setOperationResult(OperationResponse.ERROR.name());
        return returnValue;
    }
	
	@ApiOperation(value="Password Reset Request", 
			notes="Use this endpoint to request password reset")
	@PostMapping(path="/password-reset-request",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public OperationStatus requestReset(@RequestBody PasswordResetRequestModel passwordReset) {
		boolean status = userService.requestPasswordReset(passwordReset.getEmail());
		OperationStatus returnVal = new OperationStatus();
		returnVal.setOperationName(Operations.REQUEST_PASSWORD_RESET.name());
		returnVal.setOperationResult(OperationResponse.ERROR.name());
		if(status)
			returnVal.setOperationResult(OperationResponse.SUCCESS.name());
		
		return returnVal;
	}
	
	@ApiOperation(value="Reset your password with new password", 
			notes="Use this endpoint to save your new password, add the token as a parameter to this link like /users/password-reset?token=<tokenValue>")
	@PostMapping(path="/password-reset",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public OperationStatus passwordReset(@RequestBody PasswordResetModel details) {
		
		boolean result = userService.resetPassword(details.getToken(), details.getPassword());
		
		OperationStatus returnVal = new OperationStatus();
		returnVal.setOperationName(Operations.PASSWORD_RESET.name());
		returnVal.setOperationResult(OperationResponse.ERROR.name());
		if(result)
			returnVal.setOperationResult(OperationResponse.SUCCESS.name());
		
		return returnVal;
	}
}

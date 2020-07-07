package com.springcourse.project.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springcourse.project.ui.model.request.UserLoginRequest;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;

@RestController
public class LoginController {

	@ApiOperation("User Login")
	@ApiResponses(value={
		@ApiResponse(code=200,
				message="Response Headers",
				responseHeaders={
						@ResponseHeader(name="Authorization",
								description="Bearer prefixed JWT token",
								response=String.class),
						
						@ResponseHeader(name="userId",
						description="<Public UserId with value>",
						response=String.class)
				}
		)
	})
	@PostMapping("/users/login")
	public void fakeLogin(@RequestBody UserLoginRequest request) {
		
		throw new IllegalStateException("this method is for documentation purposes only and should not be called");
	}
	
}

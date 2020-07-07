package com.springcourse.project.ui.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

	@Autowired
	Utils utils;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	final void testGenerateUserID() {
		String userId = utils.generateUserID(30);
		String userId2 = utils.generateUserID(30);
		assertNotNull(userId);
		assertNotNull(userId2);
		assertTrue(userId.length()==30);
		assertTrue(!userId2.equalsIgnoreCase(userId));
	}

	@Test
	final void testHasTokenNotExpired() {
		String token = utils.getEmailVerificationToken("as32agt4hbnty4");
		assertNotNull(token);
		boolean status = utils.hasTokenExpired(token);
		assertFalse(status);
	}
	

}

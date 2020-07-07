package com.springcourse.project.io.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springcourse.project.io.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepo;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	final void testFindVerifiedUsers() {
		Pageable request = PageRequest.of(0, 1);
		userRepo.findAllUsersWithConfirmedEmailAddress(request);
		Page<UserEntity> page = userRepo.findAllUsersWithConfirmedEmailAddress(request);
		assertNotNull(page);
		
		List<UserEntity> list = page.getContent();
		assertNotNull(list.size() == 1);
		assertEquals(list.get(0).getFirstName() , "Vaibhav");
	}
	
	@Test
	final void testFindByFirstName() {
		String firstName = "Vaibhav";
		List<UserEntity> users = userRepo.findUserByFirstName(firstName);
		assertNotNull(users);
		assertEquals(users.size(),1);
	}
	
	@Test
	final void testFindByKeyword() {
		String keyword = "Man";
		List<Object[]> users = userRepo.findUserByKeyword(keyword);
		assertNotNull(users);
		assertTrue(String.valueOf(users.get(0)[0]).contains(keyword) || 
				String.valueOf(users.get(0)[1]).contains(keyword));
	}
	
	@Test
	final void testFindByUserId() {
		String userId = "6s5HJJ4AxhgRHkc8lKyYt7ELoSMQGA";
		
		UserEntity entity = userRepo.findUserByUserId(userId);
		
		assertNotNull(entity);
		assertEquals(entity.getUserId(), userId);
	}

}

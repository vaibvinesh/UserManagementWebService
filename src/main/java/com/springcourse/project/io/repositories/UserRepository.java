package com.springcourse.project.io.repositories;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springcourse.project.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String id);
	UserEntity findByEmailVerificationToken(String token);
	
	@Query(value= "select * from users where email_verification_status = 1",
			countQuery="select count(*) from users where email_verification_status = 'true'",
			nativeQuery=true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequst);
	
	@Query(value= "SELECT * from USERS u where u.first_name= :name", nativeQuery=true)
	List<UserEntity> findUserByFirstName(@Param("name") String firstName);
	
	@Query(value= "SELECT u.first_name , u.last_name from USERS u where u.first_name like %:keyword% or u.last_name like %:keyword%", nativeQuery=true)
	List<Object[]> findUserByKeyword(@Param("keyword") String keyword);
	
	@Query("select user from UserEntity user where userId = :userId")
	UserEntity findUserByUserId(@Param("userId") String userId);
}

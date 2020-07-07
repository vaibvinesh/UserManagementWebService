package com.springcourse.project.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.springcourse.project.io.entity.PasswordResetEntity;
@Repository
public interface PasswordResetRepo extends CrudRepository<PasswordResetEntity, Long> {
	
	PasswordResetEntity findByToken(String token);
}

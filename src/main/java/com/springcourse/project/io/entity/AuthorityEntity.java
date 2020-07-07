package com.springcourse.project.io.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="authorities")
public class AuthorityEntity implements Serializable{

	private static final long serialVersionUID = -455225523500148438L;
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private long id;
	
	
	@Column(nullable = false, length = 30)
	private String name;
	
	@ManyToMany(mappedBy="authorities")
	private List<RoleEntity> roles;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
	
	
	
}

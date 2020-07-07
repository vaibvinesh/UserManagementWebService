package com.springcourse.project.io.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class RoleEntity implements Serializable {
	
	private static final long serialVersionUID = -2333907643173698941L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false, length=20)
	private String name;

	@ManyToMany(mappedBy="roles")
	private List<UserEntity> users;
	
	@ManyToMany(cascade={CascadeType.PERSIST}, fetch=FetchType.EAGER)
	@JoinTable(name="roles_authorities", 
			joinColumns=@JoinColumn(name="roles_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name="authorities_id", referencedColumnName = "id"))
	private List<AuthorityEntity> authorities;
	
	public List<UserEntity> getUsers() {
		return users;
	}

	public List<AuthorityEntity> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<AuthorityEntity> authorities) {
		this.authorities = authorities;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

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
	
	
	
}

package com.springcourse.project.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="addresses")
public class AddressEntity implements Serializable {

	private static final long serialVersionUID = -5365753766833472562L;

	@Id
	@GeneratedValue
	private long id;
	@Column(length=30, nullable=false)
	private String addressId;
	@Column(length=200, nullable=false)
	private String building;
	@Column(length=150, nullable=false)
	private String streetName;
	@Column(length=20, nullable=false)
	private String city;
	@Column(length=10, nullable=false)
	private String postalCode;
	@Column(length=20, nullable=false)
	private String state;
	@Column(length=25, nullable=false)
	private String country;
	@Column(length=15, nullable=false)
	private String type;
	@ManyToOne
	@JoinColumn(name="users_id")
	private UserEntity userDetails;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}
}

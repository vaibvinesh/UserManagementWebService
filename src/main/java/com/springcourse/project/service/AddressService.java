package com.springcourse.project.service;

import java.util.List;

import com.springcourse.project.ui.shared.dto.AddressDTO;

public interface AddressService {
	List<AddressDTO> getAddresses(String userId);
	AddressDTO getAddress(String id);
}

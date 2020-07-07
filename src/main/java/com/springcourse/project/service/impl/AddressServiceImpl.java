package com.springcourse.project.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springcourse.project.io.entity.AddressEntity;
import com.springcourse.project.io.entity.UserEntity;
import com.springcourse.project.io.repositories.AddressRepository;
import com.springcourse.project.io.repositories.UserRepository;
import com.springcourse.project.service.AddressService;
import com.springcourse.project.ui.model.response.AddressRespUI;
import com.springcourse.project.ui.shared.dto.AddressDTO;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AddressRepository addressRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<AddressDTO> getAddresses(String userId) {
		// TODO Auto-generated method stub
		/*UserEntity entity = userRepo.findByUserId(userId);
		if(entity == null) return null;
		List<AddressDTO> returnVal = new ArrayList<>();
		Iterable<AddressEntity> addresses = addressRepo.findAllByUserDetails(entity);
		for(AddressEntity obj : addresses) {
			returnVal.add(modelMapper.map(obj, AddressDTO.class));
		}*/
		
		List<AddressEntity> details = userRepo.findByUserId(userId).getAddresses();
		java.lang.reflect.Type listType = new TypeToken<List<AddressDTO>>() {}.getType();
		List<AddressDTO> returnVal = modelMapper.map(details, listType);
		
		return returnVal;
	}

	@Override
	public AddressDTO getAddress(String id) {
		AddressEntity entity = addressRepo.findByAddressId(id);
		if(entity==null) return null;
		AddressDTO returnVal =  modelMapper.map(entity, AddressDTO.class);
		return returnVal;
	}
	
}

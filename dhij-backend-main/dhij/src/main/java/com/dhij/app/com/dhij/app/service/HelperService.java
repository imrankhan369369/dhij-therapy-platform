package com.dhij.app.com.dhij.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.dhij.app.com.dhij.app.dto.HelperDto;
import com.dhij.app.com.dhij.app.model.Helper;
import com.dhij.app.com.dhij.app.repository.HelperRepository;

public interface HelperService {
	
	
	
	
	Page<HelperDto> getAllHelpers(int page,int size,String sortBy,String sortDir);
	Optional<HelperDto> getHelperById(Long id);
	List<HelperDto> getAllHelpers();
	HelperDto addHelper(Helper helper);
	HelperDto updateHelper(Long id, Helper helper);
	void deleteHelper(Long id);
	List<HelperDto> searchHelpers(String name, String role, String specialty);

}

package com.dhij.app.com.dhij.app.mapper;

import com.dhij.app.com.dhij.app.dto.HelperDto;
import com.dhij.app.com.dhij.app.model.Helper;

public class HelperMapper {
	public static HelperDto toDto(Helper helper) {
		if(helper==null) {
			return null;
		}
		
		HelperDto dto=new HelperDto();
		
		
				dto.setId(helper.getId());
				dto.setName(helper.getName());
				dto.setRole(helper.getRole());
				dto.setSpecialty(helper.getSpecialty());
				
				return dto;
				
				
	}
}

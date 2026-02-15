package com.dhij.app.com.dhij.app.service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dhij.app.com.dhij.app.dto.HelperDto;
import com.dhij.app.com.dhij.app.exception.HelperNotFoundException;
import com.dhij.app.com.dhij.app.mapper.HelperMapper;
import com.dhij.app.com.dhij.app.model.Helper;
import com.dhij.app.com.dhij.app.repository.HelperRepository;

 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
import java.util.Optional;
 
@Service
public class HelperServiceImpl implements HelperService {
 
    private static final Logger logger =
            LoggerFactory.getLogger(HelperServiceImpl.class);
 
    private final HelperRepository helperRepository;
 
    public HelperServiceImpl(HelperRepository helperRepository) {
        this.helperRepository = helperRepository;
    }
 
    // =========================
    // ADD HELPER
    // =========================
    @Override
    public HelperDto addHelper(Helper helper) {
 
        logger.info("Adding helper with name: {}", helper.getName());
 
        // Important: ensure ID is null so Hibernate auto-generates it
        helper.setId(null);
 
        Helper savedHelper = helperRepository.save(helper);
 
        logger.info("Helper saved successfully with ID: {}", savedHelper.getId());
 
        return HelperMapper.toDto(savedHelper);
    }
 
    // =========================
    // GET HELPER BY ID
    // =========================
    @Override
    public Optional<HelperDto> getHelperById(Long id) {
 
        logger.info("Fetching helper with ID: {}", id);
 
        return helperRepository.findById(id)
                .map(HelperMapper::toDto)
                .or(() -> {
                    throw new HelperNotFoundException("Helper", "id", id);
                });
    }
 
    // =========================
    // UPDATE HELPER
    // =========================
    @Override
    public HelperDto updateHelper(Long id, Helper helper) {
 
        logger.info("Updating helper with ID: {}", id);
 
        Helper existingHelper = helperRepository.findById(id)
                .orElseThrow(() ->
                        new HelperNotFoundException("Helper", "id", id)
                );
 
        existingHelper.setName(helper.getName());
        existingHelper.setRole(helper.getRole());
        existingHelper.setSpecialty(helper.getSpecialty());
 
        Helper updatedHelper = helperRepository.save(existingHelper);
 
        logger.info("Helper updated successfully with ID: {}", id);
 
        return HelperMapper.toDto(updatedHelper);
    }
 
    // =========================
    // DELETE HELPER
    // =========================
    @Override
    public void deleteHelper(Long id) {
 
        logger.info("Deleting helper with ID: {}", id);
 
        Helper helper = helperRepository.findById(id)
                .orElseThrow(() ->
                        new HelperNotFoundException("Helper", "id", id)
                );
 
        helperRepository.delete(helper);
 
        logger.info("Helper deleted successfully with ID: {}", id);
    }

	@Override
	public List<HelperDto> getAllHelpers() {
		List<Helper> helpers=helperRepository.findAll();
		return helpers.stream().map(HelperMapper::toDto).toList();
	}

	@Override
	public Page<HelperDto> getAllHelpers(int page, int size, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():
			Sort.by(sortBy).descending();
		Pageable pageable=PageRequest.of(page, size,sort);
		Page<Helper> helpers=helperRepository.findAll(pageable);
		return helpers.map(HelperMapper::toDto);
	}
	

@Override
public List<HelperDto> searchHelpers(String name, String role, String specialty) {
    boolean hasName = name != null && !name.isBlank();
    boolean hasRole = role != null && !role.isBlank();
    boolean hasSpecialty = specialty != null && !specialty.isBlank();

    // Pull from DB (you can optimize to DB-side queries later)
    List<Helper> helpers = helperRepository.findAll();

    return helpers.stream()
        .filter(h -> !hasName || (h.getName() != null &&
                h.getName().toLowerCase().contains(name.toLowerCase())))
        .filter(h -> !hasRole || (h.getRole() != null &&
                h.getRole().equalsIgnoreCase(role)))
        .filter(h -> !hasSpecialty || (h.getSpecialty() != null &&
                h.getSpecialty().equalsIgnoreCase(specialty)))
        .map(HelperMapper::toDto)
        .toList();
}

	
}

 
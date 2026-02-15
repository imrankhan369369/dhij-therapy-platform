package com.dhij.app.com.dhij.app.controller;
//import jakarta.validation.*;
import com.dhij.app.com.dhij.app.dto.HelperDto;
import com.dhij.app.com.dhij.app.exception.HelperNotFoundException;
import com.dhij.app.com.dhij.app.model.Helper;
import com.dhij.app.com.dhij.app.repository.HelperRepository;
import com.dhij.app.com.dhij.app.response.ApiResponse;
import com.dhij.app.com.dhij.app.service.HelperService;

import ch.qos.logback.classic.Logger;
import jakarta.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/helpers")
public class HelperController {
	private static final Logger logger=(Logger) LoggerFactory.getLogger(HelperController.class);
    @Autowired
  // private HelperRepository helperRepository;
    private  final HelperService helperService;
    
    public HelperController(HelperService helperService) {
    	this.helperService=helperService;
    }

    // Get all helpers
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<HelperDto>>> getAllHelpers() {
    	
    	List<HelperDto> helpers = helperService.getAllHelpers();
    	ApiResponse<List<HelperDto>> response=new ApiResponse<>(200,"Helper fetched successfully",helpers);
    	
        
       // return helperService.getAllHelpers();
    	return ResponseEntity.ok(response);
        }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HelperDto>> updateHelper(
            @PathVariable Long id,
           @Valid @RequestBody Helper helper) {
    	logger.info("Helper with ID{} updated succesfully",id);
    	HelperDto dto=helperService.updateHelper(id, helper);
    	ApiResponse<HelperDto> response=new ApiResponse<>(200,"Helper updated succesfully",dto);
        return ResponseEntity.ok(response);
    }
 
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteHelper(@PathVariable Long id) {
    	logger.info("Request to delete helper with Id:{}", id);
        helperService.deleteHelper(id);
        ApiResponse<Void> response=new ApiResponse<>(200,"Helper Deleted successfully",null);
        //logger.info("Helper with ID{} deleted successfully ",id);
        return ResponseEntity.ok(response);
    }
    
    

    // Add a new helper
    
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HelperDto>> addHelper(@Valid @RequestBody Helper helper) {
    	HelperDto dto= helperService.addHelper(helper);
    	//final Logger logger=(Logger) LoggerFactory.getLogger(HelperController.class);
    	logger.info("Helper added succesfully with name={}",dto.getName());
    	ApiResponse<HelperDto> response= new ApiResponse<>(201,"Helper created successfully",dto);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<HelperDto>>> getAllHelpers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Page<HelperDto> helpers =
                helperService.getAllHelpers(page, size, sortBy, sortDir);
     
        ApiResponse<Page<HelperDto>> response =
                new ApiResponse<>(200, "Helpers fetched successfully", helpers);
     
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HelperDto>>> searchHelpers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String specialty
    ) 
    
    {

    	// 400 if all criteria are missing or blank
    	        boolean noCriteria = (name == null || name.isBlank())
    	                && (role == null || role.isBlank())
    	                && (specialty == null || specialty.isBlank());
    	        if (noCriteria) {
    	            ApiResponse<List<HelperDto>> bad = new ApiResponse<>(400,
    	                    "At least one search parameter (name, role, specialty) is required", List.of());
    	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bad);
    	        }

List<HelperDto> helpers = helperService.searchHelpers(name, role, specialty);

        // 400 if nothing matched (as per your requirement)
        if (helpers.isEmpty()) {
            ApiResponse<List<HelperDto>> bad = new ApiResponse<>(400,
                    "No helpers matched the provided criteria", helpers);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bad);
        }

    	
    	
    	
       // List<HelperDto> helpers = helperService.searchHelpers(name, role, specialty);
     
        ApiResponse<List<HelperDto>> response =
                new ApiResponse<>(200, "Helpers fetched successfully", helpers);
     
        return ResponseEntity.ok(response);
    }

    // Get helper by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<HelperDto>> getHelperById(@PathVariable Long id) {
     
        HelperDto dto = helperService.getHelperById(id)
                .get();
     
        ApiResponse<HelperDto> response =
                new ApiResponse<>(200, "Helper fetched successfully", dto);
     
        return ResponseEntity.ok(response);
    }
}
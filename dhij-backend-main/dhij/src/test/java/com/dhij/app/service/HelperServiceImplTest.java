
package com.dhij.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dhij.app.com.dhij.app.model.Helper;
import com.dhij.app.com.dhij.app.exception.HelperNotFoundException;
import com.dhij.app.com.dhij.app.repository.HelperRepository;
import com.dhij.app.com.dhij.app.service.HelperServiceImpl;
import com.dhij.app.com.dhij.app.dto.HelperDto;

@ExtendWith(MockitoExtension.class)
class HelperServiceImplTest {

    @Mock
    private HelperRepository helperRepository;

    @InjectMocks
    private HelperServiceImpl helperService;

    @Test
    void getAllHelpers_shouldReturnListOfDtos() {
        Helper helper = new Helper();
        helper.setId(1L);
        helper.setName("Rahul");
        helper.setRole("Doctor");
        helper.setSpecialty("Cardiology");

        when(helperRepository.findAll()).thenReturn(List.of(helper));

        List<HelperDto> result = helperService.getAllHelpers();

        assertEquals(1, result.size());
        assertEquals("Rahul", result.get(0).getName());
        assertEquals("Doctor", result.get(0).getRole());
        assertEquals("Cardiology", result.get(0).getSpecialty());

        verify(helperRepository, times(1)).findAll();
        verifyNoMoreInteractions(helperRepository);
    }

    @Test
    void getHelperById_shouldReturnOptional_whenFound() {
        Helper helper = new Helper();
        helper.setId(1L);
        helper.setName("Asha");
        helper.setRole("Nurse");
        helper.setSpecialty("Pediatrics");

        when(helperRepository.findById(1L)).thenReturn(Optional.of(helper));

        Optional<HelperDto> result = helperService.getHelperById(1L);

        assertTrue(result.isPresent());
        assertEquals("Asha", result.get().getName());
        assertEquals("Nurse", result.get().getRole());
        assertEquals("Pediatrics", result.get().getSpecialty());

        verify(helperRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(helperRepository);
    }

    @Test
    void getHelperById_shouldThrow_whenNotFound() {
        when(helperRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HelperNotFoundException.class,
                () -> helperService.getHelperById(1L));

        verify(helperRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(helperRepository);
    }
}

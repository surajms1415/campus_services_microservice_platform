package com.campusservices.facility.service;

import com.campusservices.facility.dto.FacilityDto;
import com.campusservices.facility.dto.FacilityRequest;
import com.campusservices.facility.entity.Facility;
import com.campusservices.facility.repository.FacilityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceTest {

    @Mock private FacilityRepository repository;
    @InjectMocks private FacilityService service;

    @Test
    void createFacility_Success() {
        // Arrange
        FacilityRequest req = new FacilityRequest();
        req.setName("Lab 1");
        
        Facility saved = new Facility();
        saved.setId(1L);
        saved.setName("Lab 1");
        
        when(repository.save(any(Facility.class))).thenReturn(saved);
        
        // Act
        FacilityDto res = service.createFacility(req);
        
        // Assert
        assertNotNull(res);
        assertEquals(1L, res.getId());
        assertEquals("Lab 1", res.getName());
    }

    @Test
    void getFacilityById_Success() {
        // Arrange
        Facility facility = new Facility();
        facility.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(facility));
        
        // Act
        FacilityDto res = service.getFacilityById(1L);
        
        // Assert
        assertNotNull(res);
        assertEquals(1L, res.getId());
    }

    @Test
    void updateFacility_Success() {
        // Arrange
        Facility existing = new Facility();
        existing.setId(1L);
        existing.setName("Old Name");
        
        FacilityRequest req = new FacilityRequest();
        req.setName("New Name");
        
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Facility.class))).thenReturn(existing);
        
        // Act
        FacilityDto res = service.updateFacility(1L, req);
        
        // Assert
        assertEquals("New Name", res.getName());
    }

    @Test
    void deleteFacility_Success() {
        // Arrange & Act
        service.deleteFacility(1L);
        // Assert
        verify(repository, times(1)).deleteById(1L);
    }
}

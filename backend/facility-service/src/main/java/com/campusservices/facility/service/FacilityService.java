package com.campusservices.facility.service;

import com.campusservices.facility.dto.FacilityDto;
import com.campusservices.facility.dto.FacilityRequest;
import com.campusservices.facility.entity.Facility;
import com.campusservices.facility.repository.FacilityRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {
    private final FacilityRepository repository;

    public FacilityService(FacilityRepository repository) {
        this.repository = repository;
    }

    // Cache the paginated search results. Key includes search query and pagination details.
    @Cacheable(value = "facilities_page", key = "#search + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<FacilityDto> getFacilities(String search, Pageable pageable) {
        Page<Facility> facilities;
        if (search != null && !search.trim().isEmpty()) {
            facilities = repository.searchFacilities(search, pageable);
        } else {
            facilities = repository.findAll(pageable);
        }
        return facilities.map(this::mapToDto);
    }

    // Cache individual facility details
    @Cacheable(value = "facility", key = "#id")
    public FacilityDto getFacilityById(Long id) {
        return mapToDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Facility not found")));
    }

    // Invalidate list caches when a new facility is created
    @CacheEvict(value = "facilities_page", allEntries = true)
    public FacilityDto createFacility(FacilityRequest req) {
        Facility facility = new Facility();
        updateEntity(facility, req);
        return mapToDto(repository.save(facility));
    }

    // Invalidate list caches and the specific facility cache when updated
    @Caching(evict = {
            @CacheEvict(value = "facilities_page", allEntries = true),
            @CacheEvict(value = "facility", key = "#id")
    })
    public FacilityDto updateFacility(Long id, FacilityRequest req) {
        Facility facility = repository.findById(id).orElseThrow(() -> new RuntimeException("Facility not found"));
        updateEntity(facility, req);
        return mapToDto(repository.save(facility));
    }

    // Invalidate list caches and the specific facility cache when deleted
    @Caching(evict = {
            @CacheEvict(value = "facilities_page", allEntries = true),
            @CacheEvict(value = "facility", key = "#id")
    })
    public void deleteFacility(Long id) {
        repository.deleteById(id);
    }

    private void updateEntity(Facility facility, FacilityRequest req) {
        facility.setName(req.getName());
        facility.setDescription(req.getDescription());
        facility.setLocation(req.getLocation());
        facility.setCapacity(req.getCapacity());
        facility.setAvailable(req.getAvailable());
    }

    private FacilityDto mapToDto(Facility facility) {
        FacilityDto dto = new FacilityDto();
        dto.setId(facility.getId());
        dto.setName(facility.getName());
        dto.setDescription(facility.getDescription());
        dto.setLocation(facility.getLocation());
        dto.setCapacity(facility.getCapacity());
        dto.setAvailable(facility.getAvailable());
        dto.setCreatedAt(facility.getCreatedAt());
        return dto;
    }
}

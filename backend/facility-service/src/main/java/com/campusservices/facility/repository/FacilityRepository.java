package com.campusservices.facility.repository;

import com.campusservices.facility.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    @Query("SELECT f FROM Facility f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(f.location) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Facility> searchFacilities(@Param("search") String search, Pageable pageable);
}

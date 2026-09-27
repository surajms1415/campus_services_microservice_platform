package com.campusservices.facility.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FacilityDto {
    private Long id;
    private String name;
    private String description;
    private String location;
    private Integer capacity;
    private Boolean available;
    private LocalDateTime createdAt;
}

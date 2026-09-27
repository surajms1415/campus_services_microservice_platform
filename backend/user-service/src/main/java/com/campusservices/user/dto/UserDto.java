package com.campusservices.user.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.campusservices.user.entity.User.Role;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}

package com.sandeep.authentication.util;

import com.sandeep.authentication.dto.UserDTO;
import com.sandeep.authentication.entity.User;

public class EntityToUserMapper {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .subscription(user.getSubscription())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

package com.exercise.nisum.mapper;

import com.exercise.nisum.model.User;
import com.exercise.nisum.request.user.CreateUserRequest;
import com.exercise.nisum.request.user.UpdateUserRequest;
import com.exercise.nisum.response.user.UserResponse;
import com.exercise.nisum.util.DateUtil;
import lombok.Builder;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Builder
public class UserMapper {

    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    public static User toEntity(UpdateUserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    public static UserResponse toDto(User entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .isActive(entity.getIsActive())
                .token(entity.getToken())
                .lastLogin(DateUtil.formatDate(entity.getLastLogin()))
                .created(DateUtil.formatDate(entity.getCreatedAt()))
                .updated(DateUtil.formatDate(entity.getUpdatedAt()))
                .phones(entity.getPhones() != null ? entity.getPhones().stream().map(PhoneMapper::toDto).collect(Collectors.toList()) : null)
                .build();
    }

}

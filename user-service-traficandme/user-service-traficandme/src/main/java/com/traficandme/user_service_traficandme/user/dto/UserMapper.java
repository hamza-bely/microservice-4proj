package com.traficandme.user_service_traficandme.user.dto;

import com.traficandme.user_service_traficandme.common.Role;
import com.traficandme.user_service_traficandme.user.entity.UserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public UserInfo toModel(UserRequest request) {
        return new UserInfo(
                request.id(),
                request.firstName(),
                request.lastName(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.valueOf(request.role())
        );

    }

    public UserResponse toResponse(UserInfo userInfo) {
        return new UserResponse(
                userInfo.getId(),
                userInfo.getFirstName() +" "+ userInfo.getLastName(),
                userInfo.getEmail(),
                userInfo.getRoles().name(),
                userInfo.getStatus().name(),
                userInfo.getCreateDate(),
                userInfo.getUpdateDate()
        );
    }
}

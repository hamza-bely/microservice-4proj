package com.traficandme.user_service_traficandme.user.dto;

import java.util.Date;

public record UserResponse(
        Integer id,
        String username,
        String email,
        String role,
        String status,
        Date createDate,
        Date updateDate
) {
}

package com.exercise.nisum.response.user;

import com.exercise.nisum.response.phone.PhoneReponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Boolean isActive;
    private String token;
    private String lastLogin;
    private String created;
    private String updated;
    private List<PhoneReponse> phones;
}

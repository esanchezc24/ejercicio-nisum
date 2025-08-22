package com.exercise.nisum.response.phone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneReponse {
    private UUID id;
    private String number;
    private String cityCode;
    private String countryCode;
}

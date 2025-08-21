package com.exercise.nisum.response.phone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneReponse {
    private Long id;
    private String number;
    private String cityCode;
    private String countryCode;
}

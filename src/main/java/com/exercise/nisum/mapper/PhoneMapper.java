package com.exercise.nisum.mapper;

import com.exercise.nisum.model.Phone;
import com.exercise.nisum.request.phone.CreatePhoneRequest;
import com.exercise.nisum.response.phone.PhoneReponse;
import lombok.Builder;

import java.util.stream.Collectors;

@Builder
public class PhoneMapper {

    public static Phone toEntity(CreatePhoneRequest request) {
        return Phone.builder()
                .number(request.number())
                .cityCode(request.citycode())
                .countryCode(request.contrycode())
                .build();
    }


    public static PhoneReponse toDto(Phone entity) {
        return PhoneReponse.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .cityCode(entity.getCityCode())
                .countryCode(entity.getCountryCode())
                .build();
    }

}

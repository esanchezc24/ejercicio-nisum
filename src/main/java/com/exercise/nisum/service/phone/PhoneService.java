package com.exercise.nisum.service.phone;

import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;

import java.util.List;
import java.util.UUID;

public interface PhoneService {
    void savePhones(List<Phone> phones, User user);

    void deletePhonesByUserId(UUID userId);

    List<Phone> findPhonesByUserId(UUID userId);
}

package com.exercise.nisum.service.phone;

import com.exercise.nisum.common.CrudCommon;
import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;

import java.util.List;

public interface PhoneService {
    void savePhones(List<Phone> phones, User user);

    void deletePhonesByUserId(Long userId);

    List<Phone> findPhonesByUserId(Long userId);
}


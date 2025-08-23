package com.exercise.nisum.service.phone;

import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;
import com.exercise.nisum.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    private final PhoneRepository repository;


    @Override
    public void savePhones(List<Phone> phones, User user) {
        phones.forEach(phone -> {
           phone.setUser(user);
           repository.save(phone);
        });
    }

    @Override
    public void deletePhonesByUserId(UUID userId) {
        repository.deleteByUserId(userId);
    }

    @Override
    public List<Phone> findPhonesByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }
}

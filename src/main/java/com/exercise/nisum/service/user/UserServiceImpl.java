package com.exercise.nisum.service.user;

import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;
import com.exercise.nisum.repository.UserRepository;
import com.exercise.nisum.request.phone.CreatePhoneRequest;
import com.exercise.nisum.service.phone.PhoneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository;

    @Autowired
    private final PhoneService phoneService;


    @Override
    public User save(User entity) {
      return repository.save(entity);
    }

    @Override
    public User update(Long id, User entity) {
        User existingUser = findById(id);
        phoneService.deletePhonesByUserId(id);

        // Copiar propiedades nuevas al objeto existente, excluyendo id y createdAt
        BeanUtils.copyProperties(entity, existingUser, "id", "createdAt", "updatedAt", "deletedAt", "isActive", "token");

        existingUser.setUpdatedAt(LocalDateTime.now());

        phoneService.savePhones(entity.getPhones(), entity);

        return repository.save(entity);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        user.softDelete();
        repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User save(User entity, List<Phone> phones) {
        User savedUser = repository.save(entity);
        phoneService.savePhones(phones, savedUser);
        savedUser.setPhones(phoneService.findPhonesByUserId(savedUser.getId()));
        return savedUser;
    }
}

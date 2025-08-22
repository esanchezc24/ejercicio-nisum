package com.exercise.nisum.service.user;

import com.exercise.nisum.exception.BadRequestException;
import com.exercise.nisum.exception.ConflictException;
import com.exercise.nisum.exception.ResourceNotFoundException;
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
        validateUserEmail(null, entity.getEmail());
        return repository.save(entity);
    }

    @Override
    public User update(Long id, User entity) {
        User existingUser = findById(id);
        validateUserEmail(existingUser.getEmail(), entity.getEmail());

        BeanUtils.copyProperties(entity, existingUser,
                "id", "createdAt", "deletedAt", "password", "lastLogin", "isActive", "token", "phones");

        existingUser.setUpdatedAt(LocalDateTime.now());
        if (entity.getPassword() != null) {
            existingUser.setPassword(entity.getPassword());
        }

        return repository.save(existingUser);
    }

    @Override
    public User updateWithPhones(Long userId, User entity, List<Phone> phones) {
        phoneService.deletePhonesByUserId(userId);
        User user = update(userId, entity);
        phoneService.savePhones(phones, user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        if (!user.getIsActive()) {
            throw new BadRequestException("El usuario ya está eliminado");
        }
        user.softDelete();
        repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User saveWithPhones(User entity, List<Phone> phones) {
        User savedUser = save(entity);
        phoneService.savePhones(phones, savedUser);
        savedUser.setPhones(phoneService.findPhonesByUserId(savedUser.getId()));
        return savedUser;
    }

    private void validateUserEmail(String currentEmail, String newEmail) {
        boolean emailExists = (newEmail != null && !newEmail.isEmpty())
                ? (!newEmail.equals(currentEmail) && repository.existsByEmail(newEmail))
                : repository.existsByEmail(currentEmail);

        if (emailExists) {
            throw new ConflictException("El correo electrónico ya está registrado: " + (newEmail != null ? newEmail : currentEmail));
        }
    }
}

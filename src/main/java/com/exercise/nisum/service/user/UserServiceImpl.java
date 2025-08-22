package com.exercise.nisum.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.exercise.nisum.exception.BadRequestException;
import com.exercise.nisum.exception.ConflictException;
import com.exercise.nisum.exception.ResourceNotFoundException;
import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;
import com.exercise.nisum.repository.UserRepository;
import com.exercise.nisum.service.phone.PhoneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository;

    @Autowired
    private final PhoneService phoneService;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationTime}")
    private long expirationTime;


    @Override
    public User save(User entity) {
        validateUserEmail(null, entity.getEmail());
        User savedUser = repository.save(entity);
        String token = generateJwtToken(savedUser);
        savedUser.setToken(token);
        return repository.save(savedUser);
    }

    @Override
    public User update(UUID id, User entity) {
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
    public User updateWithPhones(UUID userId, User entity, List<Phone> phones) {
        phoneService.deletePhonesByUserId(userId);
        User user = update(userId, entity);
        phoneService.savePhones(phones, user);
        return user;
    }

    @Override
    public User restore(UUID userId) {
        User user = findById(userId);
        if (user.getIsActive()) {
            throw new BadRequestException("El usuario ya está activo");
        }
        user.setIsActive(true);
        user.setDeletedAt(null);
        return repository.save(user);
    }

    @Override
    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public void delete(UUID id) {
        User user = findById(id);
        if (!user.getIsActive()) {
            throw new BadRequestException("El usuario ya está eliminado");
        }
        user.setIsActive(false);
        user.setDeletedAt(LocalDateTime.now());
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
            throw new ConflictException("El correo electrónico ya está registrado");
        }
    }

    private String generateJwtToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("name", user.getName())
                .withClaim("id", user.getId().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }
}

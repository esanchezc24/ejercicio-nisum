package com.exercise.nisum.service;

import com.exercise.nisum.exception.BadRequestException;
import com.exercise.nisum.exception.ConflictException;
import com.exercise.nisum.exception.ResourceNotFoundException;
import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;
import com.exercise.nisum.repository.UserRepository;
import com.exercise.nisum.service.phone.PhoneService;
import com.exercise.nisum.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private UserServiceImpl userService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        ReflectionTestUtils.setField(userService, "secretKey", "nisumKey");
        ReflectionTestUtils.setField(userService, "expirationTime", 86400000L); 
    }

    @Test
    void testSaveUser_Success() {
        User user = new User();
        user.setEmail("luis@gmail.com");
        user.setPassword("password123");
        user.setName("Luis");
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void testSaveUser_EmailConflict() {
        User user = new User();
        user.setEmail("luis@gmail.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.save(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindById_UserExists() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDeleteUser_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setIsActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        assertFalse(user.getIsActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser_AlreadyDeleted() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setIsActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.delete(userId));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("test@gmail.com");
        existingUser.setPassword(passwordEncoder.encode("oldPassword"));

        User updatedUser = new User();
        updatedUser.setEmail("testupdated@gmail.com");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update(userId, updatedUser);

        assertNotNull(result);
        assertEquals("testupdated@gmail.com", result.getEmail());
        assertTrue(passwordEncoder.matches("newPassword", result.getPassword()));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_NotFound() {
        UUID userId = UUID.randomUUID();
        User updatedUser = new User();
        updatedUser.setEmail("test@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(userId, updatedUser));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindAllUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@gmail.com");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@gmail.com");

        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSaveWithPhones_Success() {
        User user = new User();
        user.setEmail("luis@gmail.com");
        user.setPassword("password123");
        user.setName("Luis");
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone();
        phone.setNumber("123456789");
        phone.setCityCode("01");
        phone.setCountryCode("1");
        phones.add(phone);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });

        User savedUser = userService.saveWithPhones(user, phones);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        verify(phoneService, times(1)).savePhones(phones, savedUser);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void testUpdateWithPhones_Success() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("test@gmail.com");

        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone();
        phone.setNumber("987654321");
        phone.setCityCode("01");
        phone.setCountryCode("1");
        phones.add(phone);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateWithPhones(userId, existingUser, phones);

        assertNotNull(updatedUser);
        verify(phoneService, times(1)).deletePhonesByUserId(userId);
        verify(phoneService, times(1)).savePhones(phones, updatedUser);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testRestoreUser_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setIsActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User restoredUser = userService.restore(userId);

        assertNotNull(restoredUser);
        assertTrue(restoredUser.getIsActive());
        assertNull(restoredUser.getDeletedAt());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRestoreUser_AlreadyActive() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setIsActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.restore(userId));
        verify(userRepository, never()).save(any(User.class));
    }
}

package com.exercise.nisum.service;

import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;
import com.exercise.nisum.repository.PhoneRepository;
import com.exercise.nisum.service.phone.PhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhoneServiceImplTest {

    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private PhoneServiceImpl phoneService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Miguel");
        user.setEmail("miguel@gmail.com");
        user.setPassword("password123");
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testSavePhones_Success() {
        List<Phone> phones = new ArrayList<>();
        Phone phone1 = new Phone();
        phone1.setNumber("123456789");
        phone1.setCityCode("01");
        phone1.setCountryCode("1");
        phones.add(phone1);

        Phone phone2 = new Phone();
        phone2.setNumber("987654321");
        phone2.setCityCode("02");
        phone2.setCountryCode("44");
        phones.add(phone2);

        phoneService.savePhones(phones, user);

        for (Phone phone : phones) {
            assertEquals(user, phone.getUser());
        }
        verify(phoneRepository, times(2)).save(any(Phone.class));
    }

    @Test
    void testDeletePhonesByUserId_Success() {
        UUID userId = user.getId();

        doNothing().when(phoneRepository).deleteByUserId(userId);

        phoneService.deletePhonesByUserId(userId);

        verify(phoneRepository, times(1)).deleteByUserId(userId);
    }

    @Test
    void testFindPhonesByUserId_Success() {
        UUID userId = user.getId();
        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone();
        phone.setNumber("123456789");
        phone.setCityCode("01");
        phone.setCountryCode("1");
        phones.add(phone);

        when(phoneRepository.findByUserId(userId)).thenReturn(phones);

        List<Phone> result = phoneService.findPhonesByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("123456789", result.getFirst().getNumber());
        verify(phoneRepository, times(1)).findByUserId(userId);
    }
}

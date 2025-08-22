package com.exercise.nisum.repository;

import com.exercise.nisum.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    void deleteByUserId(UUID userId);

    List<Phone> findByUserId(UUID userId);
}

package com.exercise.nisum.repository;

import com.exercise.nisum.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    void deleteByUserId(long userId);

    List<Phone> findByUserId(long userId);
}

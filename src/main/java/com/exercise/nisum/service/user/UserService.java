package com.exercise.nisum.service.user;

import com.exercise.nisum.common.CrudCommon;
import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService extends CrudCommon<User, UUID> {
    List<User> findAll();

    User saveWithPhones(User entity, List<Phone> phones);

    User updateWithPhones(UUID userId, User entity, List<Phone> phones);

    User restore(UUID userId);
}

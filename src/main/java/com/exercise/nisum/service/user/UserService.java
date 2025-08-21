package com.exercise.nisum.service.user;

import com.exercise.nisum.common.CrudCommon;
import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;

import java.util.List;

public interface UserService extends CrudCommon<User, Long> {
    List<User> findAll();

    User saveWithPhones(User entity, List<Phone> phones);

    User updateWithPhones(Long userId, User entity, List<Phone> phones);
}

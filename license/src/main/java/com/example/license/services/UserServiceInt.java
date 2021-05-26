package com.example.license.services;

import com.example.license.entities.User;

import java.util.Optional;

public interface UserServiceInt {

    User findUserByEmail(String email);

    User updateUser(User formUser, Optional<User> user);

    void deleteEntrepreneurUser(User user);

    void deleteInvestorUser(User user);

    User changeUserPassword(Optional<User> userOptional, User user, String currentPassword, String newPassword);

}

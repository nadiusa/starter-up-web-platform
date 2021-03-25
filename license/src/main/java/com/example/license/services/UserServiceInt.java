package com.example.license.services;

import com.example.license.entities.User;

public interface UserServiceInt {
    void changeUserPassword(User user, String newPassword);

    User findUserByEmail(String email);

    User getUserByUsername(String username);

    User updateUser(User formUser);

    void deleteEntrepreneurUser(User user);

    void deleteInvestorUser(User user);

}

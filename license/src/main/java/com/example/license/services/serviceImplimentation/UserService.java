package com.example.license.services.serviceImplimentation;

import com.example.license.entities.User;
import com.example.license.services.UserServiceInt;
import org.springframework.stereotype.Component;


@Component
public class UserService implements UserServiceInt {


    @Override
    public void changeUserPassword(User user, String newPassword) {

    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User updateUser(User formUser) {
        return null;
    }

    @Override
    public void deleteEntrepreneurUser(User user) {

    }

    @Override
    public void deleteInvestorUser(User user) {

    }
}

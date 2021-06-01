package com.example.license.services.serviceImplimentation;

import com.example.license.entities.User;
import com.example.license.exception.APIRequstException;
import com.example.license.repos.UserRepository;
import com.example.license.services.UserServiceInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService implements UserServiceInt {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User changeUserPassword(Optional<User> userOptional, User user, String currentPassword, String newPassword) {
        if (user.getPassword() != null && bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            userOptional.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
            return userOptional.get();
        } else
            throw new APIRequstException("The provided current password is incorrect.");
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(User formUser, Optional<User> user) {
        if (formUser.getBirthCountry() != null) {
            user.get().setBirthCountry(formUser.getBirthCountry());
        } else if (formUser.getBirthdate() != null) {
            user.get().setBirthdate(formUser.getBirthdate());
        } else if (formUser.getEmail() != null) {
            user.get().setEmail(formUser.getEmail());
        } else if (formUser.getFullName() != null) {
            user.get().setFullName(formUser.getFullName());
        } else if (formUser.getGender() != null) {
            user.get().setGender(formUser.getGender());
        } else if (formUser.getPassword() != null) {
            user.get().setPassword(formUser.getPassword());
        } else if (formUser.getPhoneNumber() != null) {
            user.get().setPhoneNumber(formUser.getPhoneNumber());
        }
        return user.get();
    }

    @Override
    public void deleteEntrepreneurUser(User user) {

    }

    @Override
    public void deleteInvestorUser(User user) {

    }

    public String getCurrentUserId() {
        String userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = userRepository.findByEmail(authentication.getName());
            if (user != null) {
                userId = user.getId();
            }
            return userId;
        } catch (Exception e) {
            System.out.println("Not present");
        }
        return userId;
    }

    public void updateResetPassword(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new APIRequstException("There is no user with such email:  " + email);
        }
    }

    public User get(String resetPasswordToken) {
        return userRepository.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(User user, String password) {
        String encodedPass = bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedPass);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }


}

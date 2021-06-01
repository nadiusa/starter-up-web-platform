package com.example.license.controllers;

import com.example.license.configs.JwtTokenProvider;
import com.example.license.entities.User;
import com.example.license.exception.APIRequstException;
import com.example.license.repos.UserRepository;
import com.example.license.services.MailService;
import com.example.license.services.serviceImplimentation.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    private final static Logger logger = Logger.getLogger(UserController.class.getName());
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;

    private MailService mailService;


    @Value("${siteUrl}")
    private String siteUrl;
    @Autowired
    private StorageController storageController;


    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateUser/{id}")
    public ResponseEntity updateUserInformation(@PathVariable String id, @RequestBody User user) {
        Optional<User> userRepo = userRepository.findById(id);
        logger.log(Level.INFO, "User before update information: \n" + user.toString());
        if (userRepo.isPresent()) {
            userRepository.save(userService.updateUser(user, userRepo));
            logger.log(Level.INFO, "Updated user information: \n" + userRepo.get().toString());
            return ResponseEntity.ok(userRepo.get());
        } else {
            throw new APIRequstException("Such user does not exist.");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updatePassword/{id}")
    public ResponseEntity updatePassword(@PathVariable String id, @RequestParam String currentPassword, @RequestParam String newPassword) {
        Optional<User> userRepo = userRepository.findById(id);
        User user = userRepository.findByEmail(userRepo.get().getEmail());
        userRepository.save(userService.changeUserPassword(userRepo, user, currentPassword, newPassword));
        return ResponseEntity.ok("Your password was successfully updated.");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateAvatar/{id}")
    public ResponseEntity updateUserAvatar(@RequestParam(value = "file") MultipartFile file, @PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.get();
        user.setAvatarUrl(storageController.uploadFile(file).getBody());
        userRepository.save(user);
        logger.log(Level.INFO, "Link image for user with email: " + user.getEmail() + " was added:     \n" + user.getAvatarUrl());
        return ResponseEntity.ok("Link image for user with email: " + user.getEmail() + " was added:     \n" + user.getAvatarUrl());
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.ok("The user with name '" + user.get().getFullName() + "' was deleted");
        } else
            throw new APIRequstException("Such user does not exist or you entered an invalid name!");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/resetPassword")
    public ResponseEntity resetPassword(@RequestParam @NotNull String email) {
        String token = RandomStringUtils.random(50, true, true);
        userService.updateResetPassword(token, email);
        String resetPassLink = siteUrl + "api/users/user/resetPassword?token=" + token;
        try {
            mailService.sendMail(email, resetPassLink);
            Map<Object, Object> model = new HashMap<>();
            model.put("token", token);
            return ok(model);
        } catch (UnsupportedEncodingException | MessagingException e1) {
            throw new APIRequstException(e1.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String token, @RequestParam String password) {
        User user = userService.get(token);
        if (user != null) {
            userService.updatePassword(user, password);
            logger.log(Level.INFO, "Your password was successfully reset.");
            return ResponseEntity.ok("Your password was successfully reset.");
        } else
            throw new APIRequstException("Reset password was not able to complete, please access again the link that you received on email.");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout() {
        try {
            if (jwtTokenProvider.inValidateToken())
                return "Used logged out successfully!";
            else
                return "Something went wrong!";
        } catch (AuthenticationException e) {
            throw new APIRequstException("Invalid email/password supplied");
        }
    }


}

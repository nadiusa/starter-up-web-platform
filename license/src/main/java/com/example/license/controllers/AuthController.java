package com.example.license.controllers;

import com.example.license.configs.JwtTokenProvider;
import com.example.license.entities.User;
import com.example.license.repos.UserRepository;
import com.example.license.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @Autowired
    private CustomUserDetailsService userService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthBody data) {
        try {
            String username = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.users.findByEmail(username).getRoles());
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            throw new BadCredentialsException("User with username: " + user.getEmail() + " already exists");
        }
        userService.saveUser(user);
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User registered successfully");
        return ok(model);
    }
}

//
//import com.example.license.entities.User;
//import com.example.license.services.CustomUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//public class AuthController {
//
//    @Autowired
//    private CustomUserDetailsService userService;
//
//
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public ModelAndView login() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/signup", method = RequestMethod.GET)
//    public ModelAndView signup() {
//        ModelAndView modelAndView = new ModelAndView();
//        User user = new User();
//        modelAndView.addObject("user", user);
//        modelAndView.setViewName("signup");
//        return modelAndView;
//    }
//
//
//    @RequestMapping(value = "/signup", method = RequestMethod.POST)
//    public ModelAndView createNewUser(User user, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView();
//        User userExists = userService.findUserByEmail(user.getEmail());
//        if (userExists != null) {
//            bindingResult
//                    .rejectValue("email", "error.user",
//                            "There is already a user registered with the username provided");
//            System.out.println("The error occurred, because the user with this mail/username already exists!!!!!!!!");
//        }
//        if (bindingResult.hasErrors()) {
//            modelAndView.setViewName("signup");
//        } else {
//            userService.saveUser(user);
//            modelAndView.addObject("successMessage", "User has been registered successfully.");
//            modelAndView.addObject("user", new User());
//            modelAndView.setViewName("login");
//        }
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
//    public ModelAndView dashboard() {
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEmail(auth.getName());
//        modelAndView.addObject("currentUser", user);
//        modelAndView.addObject("fullName", "Welcome " + user.getFullName());
//        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
//        modelAndView.setViewName("dashboard");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
//    public ModelAndView home() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("home");
//        return modelAndView;
//    }
//
//}

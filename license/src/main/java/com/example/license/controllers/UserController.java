package com.example.license.controllers;

import com.example.license.dto.PasswordDto;
import com.example.license.entities.User;
import com.example.license.services.serviceImplimentation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public String showForgotPasswordPage() {
        return "/forgotPassword";
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ModelAndView updatePassword(@Valid @ModelAttribute("passwordDto") PasswordDto passwordDto, BindingResult bindingResult) {

        if (!passwordDto.getPassword().equals(passwordDto.getMatchPassword())) {
            bindingResult.rejectValue("password", "password", "Passwords don't match.");
        }

        if (bindingResult.hasErrors()) {
            ModelMap model = new ModelMap();
            model.addAttribute("passwordDto", passwordDto);
            return new ModelAndView("/updatePassword", model);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changeUserPassword(user, new BCryptPasswordEncoder().encode(passwordDto.getPassword()));

        return new ModelAndView("redirect:/");
    }

}

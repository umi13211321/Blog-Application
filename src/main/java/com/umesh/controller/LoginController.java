package com.umesh.controller;

import com.umesh.entity.Post;
import com.umesh.entity.User;
import com.umesh.service.PostService;
import com.umesh.service.UserService;
import com.umesh.users.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        return "loginPage";
    }

    @RequestMapping("/showSignUpPage")
    public String showSignUpPage(Model theModel) {
        User theUser = new User();
        theModel.addAttribute("user", theUser);
        return "registrationForm";
    }

    @RequestMapping("/processRegistrationForm")
    public String processRegistrationForm(@ModelAttribute("user") UserDetails userDetails, Model theModel) {
        User existing = userService.findByUserName(userDetails.getUsername());
        if (existing != null) {
            theModel.addAttribute("user", userDetails);
            return "redirect:/showSignUpPage";
        } else {
            userService.save(userDetails);
            return "redirect:/dashboard";
        }
    }
}



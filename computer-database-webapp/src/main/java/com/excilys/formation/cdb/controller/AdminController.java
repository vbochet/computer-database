package com.excilys.formation.cdb.controller;

import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.formation.cdb.model.User;
import com.excilys.formation.cdb.service.UserService;

@Controller
@Component("userControllerBean")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/")
    public ModelAndView viewAll(@RequestParam Map<String, String> parameters) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin");
        mav.addObject("usersList", userService.listUsers());
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@RequestParam Map<String, String> parameters) {
        String username = parameters.get("username");
        String password = parameters.get("password");
        String role     = parameters.get("role");

        User user = new User(username, role, password);
        user.setEnabled(true);
        userService.createUser(user);

        return viewAll(parameters);
    }

    @GetMapping("/add")
    public ModelAndView addGet(@RequestParam Map<String, String> parameters) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addUser");
        return mav;
    }

    @PostMapping("/edit")
    public ModelAndView editPost(@RequestParam Map<String, String> parameters) {
        String username = parameters.get("username");
        String password = parameters.get("password");
        String role     = parameters.get("role");
        boolean enabled = Boolean.getBoolean(parameters.get("enabled"));

        User user = new User(username, role, password);
        user.setEnabled(enabled);
        userService.updateUser(user);

        return viewAll(parameters);
    }

    @GetMapping("/edit")
    public ModelAndView editGet(@RequestParam Map<String, String> parameters) throws ServletException {
        ModelAndView mav = new ModelAndView();
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(parameters.get("username"));
        } catch (UsernameNotFoundException e) {
            LOGGER.error("No user with username {}", parameters.get("username"));
            mav.setViewName("404");
            return mav;
        }

        mav.addObject("userDetails", userDetails);
        mav.setViewName("editUser");

        return mav;
    }

    @GetMapping("/delete")
    public ModelAndView deleteGet(@RequestParam Map<String, String> parameters) {
        String username = parameters.get("username");
        userService.deleteUser(username);

        return viewAll(parameters);
    }
}

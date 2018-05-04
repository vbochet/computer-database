package com.excilys.formation.cdb.controller;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Component("authControllerBean")
public class AuthController {

	@GetMapping("/login")
    public ModelAndView login(Locale locale, @RequestParam Map<String, String> parameters) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");

        Locale currentLocale = LocaleContextHolder.getLocale();
        mav.addObject("locale", currentLocale);

        mav.addObject("error", parameters.get("error"));
        mav.addObject("logout", parameters.get("logout"));

        return mav;
	}
}

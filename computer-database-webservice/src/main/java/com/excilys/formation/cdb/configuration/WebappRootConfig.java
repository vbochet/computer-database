package com.excilys.formation.cdb.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.excilys.formation.cdb.configuration", "com.excilys.formation.cdb.controller"})
@Import({BindingConfig.class, 
    PersistenceConfig.class, 
    ServiceConfig.class})
public class WebappRootConfig implements WebMvcConfigurer {}

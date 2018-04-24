package com.excilys.formation.cdb.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.excilys.formation.cdb.ui"})
@Import({BindingConfig.class, 
         PersistenceConfig.class, 
         ServiceConfig.class})
public class CliConfig {
}

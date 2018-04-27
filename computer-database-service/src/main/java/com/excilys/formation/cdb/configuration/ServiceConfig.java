package com.excilys.formation.cdb.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import(PersistenceConfig.class)
@ComponentScan({"com.excilys.formation.cdb.service",
                "com.excilys.formation.cdb.validator"})
@EnableTransactionManagement
public class ServiceConfig {
}

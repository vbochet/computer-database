package com.excilys.formation.cdb.configuration;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
//@PropertySource("classpath:db.properties")
@ComponentScan(basePackages = {"com.excilys.formation.cdb.controller",
                               "com.excilys.formation.cdb.dao",
                               "com.excilys.formation.cdb.dto",
                               "com.excilys.formation.cdb.mapper",
                               "com.excilys.formation.cdb.service",
                               "com.excilys.formation.cdb.servlets"})
public class WebappConfig implements WebMvcConfigurer {
//    @Value("${jdbc.driver}")
    private String driverClassName;

//    @Value("${jdbc.url}")
    private String url;

//    @Value("${jdbc.username}")
    private String username;

//    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        Resource resource = new ClassPathResource("db.properties");
        Properties props;
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driverClassName = props.getProperty("jdbc.driver");
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        password = props.getProperty("jdbc.password");
        DriverManagerDataSource dmds = new DriverManagerDataSource(url, username, password);
        dmds.setDriverClassName(driverClassName);
        return dmds;
    }

    @Bean
    public DataSourceTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public CookieLocaleResolver localeResolver(){
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        localeResolver.setCookieName("my-locale-cookie");
        localeResolver.setCookieMaxAge(3600);
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeInterceptor(){
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }

  @Bean
  public ViewResolver viewResolver() {
      InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
      viewResolver.setViewClass(JstlView.class);
      viewResolver.setPrefix("/WEB-INF/JSP/");
      viewResolver.setSuffix(".jsp");

      return viewResolver;
  }

}

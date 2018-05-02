package com.excilys.formation.cdb.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @SuppressWarnings("deprecation")
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // ensure the passwords are encoded properly
        UserBuilder users = User.withDefaultPasswordEncoder();
        auth
            .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select users.username as username, users.password as password, users.enabled as enabled \n" + 
                		"  from users where users.username=?")
                .authoritiesByUsernameQuery("SELECT users.username as username, user_roles.role as role \n" + 
                		"        FROM users \n" + 
                		"        INNER JOIN user_roles ON users.username = user_roles.username \n" + 
                		"        WHERE users.username = ?")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login*")
                    .anonymous()
                .antMatchers("/resources/**", "/403", "/404", "/500")
                    .permitAll()
                .antMatchers("/computer/**")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/dashboard/**")
                    .access("hasRole('ROLE_USER')")
                .and()
            .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .loginProcessingUrl("/j_spring_security_check")
                .defaultSuccessUrl("/dashboard")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
            .logout()
                .logoutSuccessUrl("/login?logout")
                .logoutUrl("/logout")
                .and()
            .exceptionHandling()
                .accessDeniedPage("/403")
                .and()
            .csrf();
    }
}
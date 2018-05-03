package com.excilys.formation.cdb.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT users.username as username, users.password as password, users.enabled as enabled \n" +
                                      "FROM users WHERE users.username=?")
                .authoritiesByUsernameQuery("SELECT users.username as username, users.role as role \n" +
                                            "FROM users WHERE users.username = ?")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

	@Bean
	public DigestAuthenticationEntryPoint digestEntryPoint ()
	{
	    DigestAuthenticationEntryPoint digestAuthenticationEntryPoint = new DigestAuthenticationEntryPoint();
	    digestAuthenticationEntryPoint.setKey("cdb");
	    digestAuthenticationEntryPoint.setRealmName("computer-database");
	    return digestAuthenticationEntryPoint;
	}

    public DigestAuthenticationFilter digestAuthenticationFilter (DigestAuthenticationEntryPoint digestAuthenticationEntryPoint) throws Exception
    {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
        digestAuthenticationFilter.setUserDetailsService(userDetailsServiceBean());
        return digestAuthenticationFilter;
    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login*")
                    .anonymous()
                .antMatchers("/static/**", "/403", "/404", "/500")
                    .permitAll()
                .antMatchers("/computer/**")
                    .hasRole("ADMIN")
                .antMatchers("/dashboard/**")
                    .hasAnyRole("USER", "ADMIN")
                .anyRequest()
                    .authenticated()
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
            .csrf()
                .and()
            .addFilter(digestAuthenticationFilter(digestEntryPoint()));;
    }
}
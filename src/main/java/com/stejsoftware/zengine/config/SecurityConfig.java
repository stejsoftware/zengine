package com.stejsoftware.zengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize -> authorize
                .antMatchers("/socket.io/**"    ).permitAll()
                .antMatchers("/cpu/**"          ).permitAll()
                .antMatchers("/css/**", "/index").permitAll()
                .antMatchers("/user/**"         ).hasRole("USER")
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/login-error")
            );
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers(HttpMethod.POST, "/socket.io/**");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("USER").build();

        return new InMemoryUserDetailsManager(userDetails);
    }
    // @formatter:on
}
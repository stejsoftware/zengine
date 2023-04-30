package com.stejsoftware.zengine.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests((authz) -> authz
//				.antMatchers("/api").authenticated()
				.anyRequest().permitAll()
			)
			.formLogin()
			.and()
			.build();
	}
}

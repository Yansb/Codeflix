package com.yansb.admin.api.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    public static final String ROLE_ADMIN = "CATALOGO_ADMIN";
    public static final String ROLE_CATEGORIES = "CATALOGO_CATEGORIES";
    public static final String ROLE_VIDEOS = "CATALOGO_VIDEOS";
    public static final String ROLE_CAST_MEMBERS = "CATALOGO_CAST_MEMBERS";
    public static final String ROLE_GENRES = "CATALOGO_GENRES";


    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> {
                    csrf.disable();
                })
                .authorizeHttpRequests(authorize -> {

                    authorize
                            .antMatchers("/cast_members*").hasAnyRole(ROLE_ADMIN, ROLE_CAST_MEMBERS)
                            .antMatchers("/genres*").hasAnyRole(ROLE_ADMIN, ROLE_GENRES)
                            .antMatchers("/categories*").hasAnyRole(ROLE_ADMIN, ROLE_CATEGORIES)
                            .antMatchers("/videos*").hasAnyRole(ROLE_ADMIN, ROLE_VIDEOS)
                            .anyRequest().hasRole(ROLE_ADMIN);
                })
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .headers(headers -> {
                    headers.frameOptions().sameOrigin();
                })
                .build();
    }
}

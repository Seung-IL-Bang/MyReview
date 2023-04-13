package com.web.myreview.config.auth;

import com.web.myreview.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig  {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .formLogin().disable()
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()
                )
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);

        return http.build();



    }

}

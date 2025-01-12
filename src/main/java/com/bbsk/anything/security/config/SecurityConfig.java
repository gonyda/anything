package com.bbsk.anything.security.config;

import com.bbsk.anything.security.serivce.Sha512CustomPasswordEncoder;
import com.bbsk.anything.security.serivce.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Autowired
    private UserDetailService userDetailService;

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/").permitAll() // 메인페이지
                        .requestMatchers("/join").permitAll() // 회원가입 페이지
                        .requestMatchers("/join-process").permitAll() // 회원가입 로직 url
                        .requestMatchers("/ticker/**").permitAll() // 기업 실적
                        .requestMatchers("/testApi").permitAll() // 임시
                        .requestMatchers("/fragments/**", "/css/**", "/js/**", "/img/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login") // 로그인 페이지
                        .loginProcessingUrl("/login-process") // 로그인 로직 url
                        .usernameParameter("userId")
                        .passwordParameter("userPw")
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 리다이렉트
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout").permitAll()   // 로그아웃 처리 URL (= form action url)
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new Sha512CustomPasswordEncoder();
    }
}
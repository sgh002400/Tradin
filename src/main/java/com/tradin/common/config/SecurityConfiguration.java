package com.tradin.common.config;

import com.tradin.common.filter.JwtAuthenticationFilter;
import com.tradin.common.filter.JwtExceptionFilter;
import com.tradin.common.jwt.JwtUtil;
import com.tradin.common.secret.SecretKeyManager;
import com.tradin.common.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final SecretKeyManager secretKeyManager;

    private final String[] SWAGGER_PATTERN = {"/swagger-ui", "/api-docs"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .mvcMatchers("/health-check").permitAll()
                .mvcMatchers("/v1/auth/cognito").permitAll()
                .mvcMatchers("/v1/strategies/future", "/v1/strategies/spot").permitAll()
                .mvcMatchers("/v1/histories").permitAll()
                .mvcMatchers(SWAGGER_PATTERN).authenticated()
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .httpBasic()
                .and()
                .formLogin().disable()
                .logout().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers()
                .frameOptions().sameOrigin()
                .httpStrictTransportSecurity().disable()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final String swaggerUsername = secretKeyManager.getSwaggerUsername();
        final String swaggerPassword = secretKeyManager.getSwaggerPassword();

        auth.inMemoryAuthentication()
                .withUser(swaggerUsername).password(passwordEncoder.encode(swaggerPassword)).roles("SWAGGER");
    }
}
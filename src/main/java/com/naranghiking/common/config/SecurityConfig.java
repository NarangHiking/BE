package com.naranghiking.common.config;

import com.naranghiking.common.util.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.naranghiking.auth.filter.JwtFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    String [] permitUrls = {
            "/api/auth/login",
            "/api/user/register",
            "/api/user/reset-password",
            "/api/auth/reissue",
            "/api/error",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    String [] adminUrls = {
            "/api/mtn/**",
            "/api/track/**",
    };

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 해당 빈이 따로 없으면 Spring Security가 자동으로 InMemoryUserDetailsManager를 만ㄷ름
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException(username);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // REST API는 세션/쿠키를 사용하지 않으므로 CSRF 공격 방어를 끔
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(permitUrls).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/mtn/**", "/api/track/**", 
                                                        "/api/weather/**", "/api/sun/**","/api/board", "/api/board/{id}").permitAll()
                        .requestMatchers("/api/track/*/comment", "/api/track/*/comment/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/list").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, adminUrls).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, adminUrls).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, adminUrls).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, adminUrls).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // 도중에 401 에러가 발생하면 수행
        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint));
        return http.build();
    }

    //
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "https://*.vercel.app"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
    // 기존 PasswordEncoderFactories.createDelegatingPasswordEncoder()
    // 변경 new BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.sandeep.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.csrf().disable()
//         .cors()  // 👈 Enable CORS
//         .and()
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                 .requestMatchers("/api/users/**").authenticated()
//                 .anyRequest().permitAll()
//             )
//             .oauth2ResourceServer(oauth2 -> oauth2.jwt()); // Token from Auth Service
//         return http.build();
//     }
// }
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Explicitly set CORS
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ✅ Preflight
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

    // ✅ Explicit CORS bean for Spring Security
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Angular
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

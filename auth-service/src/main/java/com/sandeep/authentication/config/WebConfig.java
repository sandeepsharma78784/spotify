package com.sandeep.application.config; // if we remove package name , then we will get cors error coze this classs is not scan by framework due to incoorect package name.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
public class WebConfig {

//     @Bean
//     public WebMvcConfigurer corsConfigurer() {
//         System.out.println("request aayi");
//         return new WebMvcConfigurer() {
//             @Override
//             public void addCorsMappings(CorsRegistry registry) {
//                 registry.addMapping("/**")                       // allow all paths
//                         .allowedOrigins("http://localhost:4200") // allow Angular app origin
//                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                         .allowedHeaders("*")
//                         .allowCredentials(true);
//             }
//         };
//     }


    
    // additionl
     // ✅ Define CORS configuration bean
    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();

    //     configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Angular app
    //     configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //     configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    //     configuration.setAllowCredentials(true); // allow cookies/token if needed

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);

    //     return source;
    // }
}

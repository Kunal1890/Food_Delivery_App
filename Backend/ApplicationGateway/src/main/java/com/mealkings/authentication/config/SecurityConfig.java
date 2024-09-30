
package com.mealkings.authentication.config;

import org.springframework.core.io.buffer.DataBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealkings.authentication.service.CustomUserDetailsService;
import com.mealkings.authentication.utils.CustomAuthenticationHandler;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Autowired
	public CustomAuthenticationHandler authenticationHandler;

	@Autowired
	public CustomUserDetailsService detailsServiceImpl;
	

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailsServiceImpl).passwordEncoder(bCryptPasswordEncoder());
	}

    @SuppressWarnings("removal")
	@Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(auth -> auth
                .pathMatchers("/users/login").permitAll()
                .pathMatchers("/users/register").permitAll()
                .pathMatchers("/restaurant/**").permitAll()
                .pathMatchers("/cart/**").permitAll()
                .pathMatchers("/order/**").permitAll()
                .pathMatchers("/discount/**").permitAll()
                .pathMatchers("/payment/**").permitAll()
//                .pathMatchers("/customer/**").permitAll()   
//                .pathMatchers("/restaurant/**").hasAnyAuthority("Restaurant", "Customer")
                .pathMatchers("/customer/**").hasAuthority("Customer")   
                .anyExchange().authenticated())
            .formLogin(formlogin ->
            	formlogin.authenticationSuccessHandler((webFilterExchange, authentication) -> {
                    ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    // Get the user's authorities
                    List<String> authorities = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                    // Create a JSON response
                    String jsonResponse = null;
					try {
						jsonResponse = String.format(
						    "{\"success\": true, \"message\": \"Login successful\", \"username\": \"%s\", \"authorities\": %s}",
						    authentication.getName(),
						    new ObjectMapper().writeValueAsString(authorities) // Convert the list to JSON
						);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(buffer));
                })
            		)
            .logout(logout -> logout.logoutUrl("/logout"));
        
//     // Configure CORS
        http.cors(cors -> cors
            .configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                config.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source.getCorsConfiguration(request);
            })
        );

        http.csrf().disable();
        return http.build();
    }
    
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            return Mono.just(detailsServiceImpl.loadUserByUsername(username))
                .filter(userDetails -> bCryptPasswordEncoder().matches(password, userDetails.getPassword()))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()))
                .cast(Authentication.class) 
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found or invalid password")));
        };
    }
}
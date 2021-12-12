package com.sachet.securityreactive.configuration;

import com.sachet.securityreactive.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@EnableWebFluxSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtServerAuthenticationConverter mySecurityContext;

    public SecurityConfig(UserRepository userRepository,
                          AuthenticationManager authenticationManager,
                          JwtServerAuthenticationConverter mySecurityContext) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.mySecurityContext = mySecurityContext;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http){
        http
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/api/secure/save").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/secure/me").hasAnyRole("USER","ADMIN")
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(mySecurityContext);

        return http.build();
    }
}

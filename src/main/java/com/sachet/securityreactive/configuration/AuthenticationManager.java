package com.sachet.securityreactive.configuration;

import com.sachet.securityreactive.repository.UserRepository;
import com.sachet.securityreactive.service.UserService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;
    private final UserRepository userService;

    public AuthenticationManager(JwtUtil jwtUtil, UserRepository userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String userName = jwtUtil.extractUsername(token);

        return userService.findByEmail(userName)
                .flatMap(userModel -> {
                    if (jwtUtil.validateToken(token, userModel)){
                        return Mono.just(authentication);
                    }else {
                        return Mono.empty();
                    }
                });
    }
}





















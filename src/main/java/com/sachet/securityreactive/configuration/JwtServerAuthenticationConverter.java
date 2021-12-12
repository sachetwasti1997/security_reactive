package com.sachet.securityreactive.configuration;

import com.sachet.securityreactive.repository.UserRepository;
import com.sachet.securityreactive.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtServerAuthenticationConverter implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtServerAuthenticationConverter(AuthenticationManager authenticationManager,
                                            JwtUtil jwtUtil,
                                            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, org.springframework.security.core.context.SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String bearer = "Bearer ";
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(b -> b.startsWith(bearer))
                .map(subs -> subs.substring(bearer.length()))
                .flatMap(token -> {
                    String email = jwtUtil.extractUsername(token);
                    return userRepository.findByEmail(email)
                            .map(userModel -> {
                                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                                userModel.getRoles().forEach(
                                        role -> authorities.add(new SimpleGrantedAuthority(role))
                                );
                                return new UsernamePasswordAuthenticationToken(
                                        userModel.getEmail()          ,
                                        token,
                                        authorities
                                );
                            });
                })
                .flatMap(auth -> authenticationManager
                        .authenticate(auth)
                        .map(SecurityContextImpl::new)
                );
    }
}

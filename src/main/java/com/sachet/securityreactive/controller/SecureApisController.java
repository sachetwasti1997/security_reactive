package com.sachet.securityreactive.controller;

import com.sachet.securityreactive.model.UserModel;
import com.sachet.securityreactive.service.UserService;
import com.sachet.securityreactive.transferobjects.ProfileUser;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/secure")
public class SecureApisController {

    private final UserService userService;

    public SecureApisController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public Mono<ResponseEntity<String>> saveUser(@RequestBody UserModel userModel){
        return userService.saveUser(userModel)
                .map(userModel1 -> new ResponseEntity<>(userModel1, HttpStatus.OK));
    }

    @GetMapping("/me")
    public Mono<ProfileUser> getMe(@AuthenticationPrincipal Principal principal){
        return Mono.just(new ProfileUser(principal.getName()));
    }

}

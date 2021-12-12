package com.sachet.securityreactive.service;

import com.sachet.securityreactive.model.UserModel;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<String> saveUser(UserModel userModel);
    Mono<UserModel> findByEmail(String email);

}

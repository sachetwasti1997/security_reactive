package com.sachet.securityreactive.repository;

import com.sachet.securityreactive.model.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserModel, String> {

    Mono<UserModel> findByEmail(String email);

}

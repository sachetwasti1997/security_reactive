package com.sachet.securityreactive.repository;

import com.sachet.securityreactive.model.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends ReactiveMongoRepository<Role, String> {
}

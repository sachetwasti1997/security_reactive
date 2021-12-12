package com.sachet.securityreactive.service;

import com.sachet.securityreactive.configuration.JwtUtil;
import com.sachet.securityreactive.model.UserModel;
import com.sachet.securityreactive.repository.RoleRepository;
import com.sachet.securityreactive.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<String> saveUser(UserModel userModel) {
        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
//        if (userModel.getRoles() == null || userModel.getRoles().isEmpty()){
            return userRepository
                    .save(userModel)
                    .map(jwtUtil::generateToken);
//        }else{123
//            return roleRepository
//                    .saveAll(userModel.getRoles())
//                    .collectList()
//                    .flatMap(roles -> {
//                        userModel.setRoles(roles);
//                        return userRepository.save(userModel)
//                                .map(jwtUtil::generateToken);
//                    });
//        }
    }

    @Override
    public Mono<UserModel> findByEmail(String email) {
        return userRepository
                .findByEmail(email);
    }
}

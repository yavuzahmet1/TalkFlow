package com.yavuzahmet.talkflow.interceptor;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.yavuzahmet.talkflow.user.UserMapper;
import com.yavuzahmet.talkflow.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserSynchronizer {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserSynchronizer(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void synchronizeWithIdp(Jwt token) {
        log.info("Synchronizing user with IDP. Subject: {}");

    }

}

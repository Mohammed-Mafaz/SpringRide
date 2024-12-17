package com.quad.project.uber.uberApp.service;

import com.quad.project.uber.uberApp.entities.User;

public interface UserService {
    User getUserById(Long userIdFromToken);
}

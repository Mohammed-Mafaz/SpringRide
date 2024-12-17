package com.quad.project.uber.uberApp.service;

import com.quad.project.uber.uberApp.dto.DriverDto;
import com.quad.project.uber.uberApp.dto.LoginResponseDto;
import com.quad.project.uber.uberApp.dto.SignupDto;
import com.quad.project.uber.uberApp.dto.UserDto;

public interface AuthService {

    String[] login(String email, String password);

    UserDto signup(SignupDto signupDto);

    DriverDto onboardNewDriver(Long userId,String vehicleId);

    String refreshToken(String refreshToken);
}

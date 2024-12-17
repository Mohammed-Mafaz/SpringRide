package com.quad.project.uber.uberApp.services.Impl;

import com.quad.project.uber.uberApp.TestContainerConfiguration;
import com.quad.project.uber.uberApp.dto.SignupDto;
import com.quad.project.uber.uberApp.dto.UserDto;
import com.quad.project.uber.uberApp.entities.User;
import com.quad.project.uber.uberApp.enums.Role;
import com.quad.project.uber.uberApp.repository.UserRepository;
import com.quad.project.uber.uberApp.security.JwtService;
import com.quad.project.uber.uberApp.service.Impl.AuthServiceImpl;
import com.quad.project.uber.uberApp.service.Impl.DriverServiceImpl;
import com.quad.project.uber.uberApp.service.Impl.RiderServiceImpl;
import com.quad.project.uber.uberApp.service.Impl.WalletServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Import(TestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RiderServiceImpl riderService;

    @Mock
    private WalletServiceImpl walletService;

    @Mock
    private DriverServiceImpl driverService;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@example.com")
                .password("password")
                .roles(Set.of(Role.RIDER))
                .build();
    }

    @Test
    void testLogin_whenSuccess(){
        // arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        // act
        String[] tokens = authService.login(user.getEmail(),user.getPassword());

        // assert
        assertThat(tokens).hasSize(2);
        assertThat(tokens[0]).isEqualTo("accessToken");
        assertThat(tokens[1]).isEqualTo("refreshToken");
    }

    @Test
    void testSignup_whenSuccess(){
        // arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // act
        SignupDto signupDto = new SignupDto();
        signupDto.setName("testName");
        signupDto.setEmail("test@example.com");
        signupDto.setPassword("password");
        UserDto userDto = authService.signup(signupDto);

        // assert
        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo(signupDto.getEmail());

        verify(riderService).createNewRider((any(User.class)));
        verify(walletService).createNewWallet(any(User.class));
    }
}

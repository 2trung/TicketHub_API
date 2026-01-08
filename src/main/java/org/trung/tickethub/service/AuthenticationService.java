package org.trung.tickethub.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.trung.tickethub.constant.PredefinedRole;
import org.trung.tickethub.constant.TokenType;
import org.trung.tickethub.dto.authentication.*;
import org.trung.tickethub.entity.Role;
import org.trung.tickethub.entity.User;
import org.trung.tickethub.exception.AppException;
import org.trung.tickethub.exception.ErrorCode;
import org.trung.tickethub.mapper.UserMapper;
import org.trung.tickethub.repository.RoleRepository;
import org.trung.tickethub.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationService {
    final JwtService jwtService;
    final UserRepository userRepository;
    final UserMapper userMapper;
    final RoleRepository roleRepository;
    final AuthenticationManager authenticationManager;

    @Value("${security.reset-password.key-duration}")
    int resetPasswordKeyDuration;

    @Value("${security.resend-reset-email.duration}")
    int resendResetEmailDuration;

    public LoginResponse login(UserLoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setAccessToken(jwtService.generateToken(user));
            tokenResponse.setRefreshToken(jwtService.generateRefreshToken(user));

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(tokenResponse);
            loginResponse.setUser(userMapper.toUserDataResponse(user));
            return loginResponse;
        } catch (Exception e) {
            log.error("Login failed for email {}: {}", request.getEmail(), e.getMessage());
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public UserDataResponse register(UserRegisterRequest request) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        var user = userMapper.toUser(request);
        Role userRole = roleRepository.findByName(PredefinedRole.USER.toString()).orElseThrow(() -> {
            log.error("Critical error: Predefined USER role not found in database. Please check database initialization.");
            return new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        });
        user.getRoles().add(userRole);
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with email: {}", request.getEmail());
        return userMapper.toUserDataResponse(savedUser);
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        User user = userRepository.findByEmail(jwtService.extractUsername(request.getRefreshToken(), TokenType.REFRESH_TOKEN)).orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));
        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(request.getRefreshToken()).build();
    }

    public void forgotPassword(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (Boolean.TRUE.equals(user.getIsActive())) {
            log.warn("Password reset requested for inactive user: {}", email);
            throw new AppException(ErrorCode.USER_BLOCKED);
        }
        if (user.getResetPasswordExpiryTime() != null && user.getResetPasswordExpiryTime() > System.currentTimeMillis()) {
            log.warn("Password reset requested but existing token is still valid for user: {}", email);
            throw new AppException(ErrorCode.TOO_MANY_REQUESTS);
        }

        // todo: check rate limiting here
        setNewResetPasswordKey(user);
        sendResetEmail(user);
        log.info("Password reset token generated and sent to email: {}", email);
    }

    public void resendResetEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (Boolean.TRUE.equals(user.getIsActive())) {
            log.warn("Resend reset email requested for inactive user: {}", email);
            throw new AppException(ErrorCode.USER_BLOCKED);
        }

        if (user.getResetPasswordExpiryTime() != null && (user.getResetPasswordExpiryTime() - System.currentTimeMillis()) <= (long) resendResetEmailDuration * 1000 * 60) {
            log.warn("Resend reset email requested but existing token is still valid for user: {}", email);
            throw new AppException(ErrorCode.TOO_MANY_REQUESTS);
        }
        setNewResetPasswordKey(user);
        sendResetEmail(user);
    }

    private void setNewResetPasswordKey(User user) {
        String md5Hex = DigestUtils.md5DigestAsHex(
                (user.getEmail() + System.currentTimeMillis()).getBytes()
        );
        user.setResetPasswordKey(md5Hex);
        if (resetPasswordKeyDuration <= 0) resetPasswordKeyDuration = 15;
        user.setResetPasswordExpiryTime(System.currentTimeMillis() + (long) resetPasswordKeyDuration * 1000 * 60);
        userRepository.save(user);
        log.info("New password reset token generated and sent to email: {}", user.getEmail());
    }

    private void sendResetEmail(User user) {
        // todo: implement email sending logic here
        log.info("Sending password reset email to: {}", user.getEmail());
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (Boolean.FALSE.equals(user.getIsActive())) {
            log.warn("Password reset failed - inactive user: {}", request.getEmail());
            throw new AppException(ErrorCode.USER_BLOCKED);
        }
        if (user.getResetPasswordExpiryTime() == null || user.getResetPasswordExpiryTime() < System.currentTimeMillis()) {
            log.warn("Password reset failed - expired token for user: {}", user.getEmail());
            throw new AppException(ErrorCode.EXPIRED_KEY);
        }
        user.setPassword(request.getNewPassword());
        user.setResetPasswordKey(null);
        user.setResetPasswordExpiryTime(null);
        userRepository.save(user);
        log.info("Password reset successfully for user: {}", user.getEmail());
    }
}

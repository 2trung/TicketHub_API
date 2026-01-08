package org.trung.tickethub.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.trung.tickethub.dto.ApiResponse;
import org.trung.tickethub.dto.authentication.*;
import org.trung.tickethub.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/register")
    ApiResponse<UserDataResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        return ApiResponse.<UserDataResponse>builder().data(authenticationService.register(request)).build();
    }

    @PostMapping("/login")
    ApiResponse<TokenResponse> login(@RequestBody @Valid UserLoginRequest request) {
        return ApiResponse.<TokenResponse>builder().data(authenticationService.login(request)).build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<TokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ApiResponse.<TokenResponse>builder().data(authenticationService.refreshToken(request)).build();
    }


    @PostMapping("/resend-reset-email")
    ApiResponse<Void> resendOtp(@RequestParam @Valid String email) {
        authenticationService.resendResetEmail(email);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/reset-password")
    ApiResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authenticationService.resetPassword(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/forgot-password")
    ApiResponse<Void> forgotPassword(@RequestParam @Valid String email) {
        authenticationService.forgotPassword(email);
        return ApiResponse.<Void>builder().build();
    }
}

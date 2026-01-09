package org.trung.tickethub.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.trung.tickethub.dto.SuccessResponse;
import org.trung.tickethub.dto.authentication.*;
import org.trung.tickethub.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/register")
    SuccessResponse<UserDataResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        return SuccessResponse.<UserDataResponse>builder().data(authenticationService.register(request)).build();
    }

    @PostMapping("/login")
    SuccessResponse<LoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        return SuccessResponse.<LoginResponse>builder().data(authenticationService.login(request)).build();
    }

    @PostMapping("/refresh-token")
    SuccessResponse<TokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return SuccessResponse.<TokenResponse>builder().data(authenticationService.refreshToken(request)).build();
    }


    @PostMapping("/resend-reset-email")
    SuccessResponse<Void> resendOtp(@RequestParam @Valid String email) {
        authenticationService.resendResetEmail(email);
        return SuccessResponse.<Void>builder().build();
    }

    @PostMapping("/reset-password")
    SuccessResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authenticationService.resetPassword(request);
        return SuccessResponse.<Void>builder().build();
    }

    @PostMapping("/forgot-password")
    SuccessResponse<Void> forgotPassword(@RequestParam @Valid String email) {
        authenticationService.forgotPassword(email);
        return SuccessResponse.<Void>builder().build();
    }
}

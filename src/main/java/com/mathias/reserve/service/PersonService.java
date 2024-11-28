package com.mathias.reserve.service;

import com.mathias.reserve.payload.request.ForgotPasswordRequest;
import com.mathias.reserve.payload.request.LoginRequest;
import com.mathias.reserve.payload.request.RegisterRequest;
import com.mathias.reserve.payload.request.ResetPasswordRequest;
import com.mathias.reserve.payload.response.LoginResponse;
import com.mathias.reserve.payload.response.RegisterResponse;
import jakarta.mail.MessagingException;

public interface PersonService {
    RegisterResponse registerPerson(RegisterRequest registerRequest) throws Exception;

    LoginResponse loginPerson(LoginRequest loginRequest);

    String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;

    String resetPassword(ResetPasswordRequest resetPasswordRequest);

}

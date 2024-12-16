package com.mathias.reserve.service;

import com.mathias.reserve.payload.request.EmailDetails;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails, String templateName) throws MessagingException;

    void sendBookingAlert(EmailDetails emailDetails, String templateName) throws MessagingException;
}

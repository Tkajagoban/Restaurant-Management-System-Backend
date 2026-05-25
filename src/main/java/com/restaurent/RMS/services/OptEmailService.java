package com.restaurent.RMS.services;

import com.restaurent.RMS.entities.User;

public interface OptEmailService {


    void sendOtpEmail(String recipientEmail, String otp);

  //  void saveOtpForUser(User user, int otpInt);
}

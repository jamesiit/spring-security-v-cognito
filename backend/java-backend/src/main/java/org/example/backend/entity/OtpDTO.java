package org.example.backend.entity;

public class OtpDTO {

    private String otpUsername;
    private String otpString;

    public OtpDTO(String otpUsername, String otpString) {
        this.otpUsername = otpUsername;
        this.otpString = otpString;
    }

    public String getOtpUsername() {
        return otpUsername;
    }

    public void setOtpUsername(String otpUsername) {
        this.otpUsername = otpUsername;
    }

    public String getOtpString() {
        return otpString;
    }

    public void setOtpString(String otpString) {
        this.otpString = otpString;
    }

}

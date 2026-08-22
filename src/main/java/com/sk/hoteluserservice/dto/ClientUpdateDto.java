package com.sk.hoteluserservice.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

public class ClientUpdateDto {
    @NotBlank
    private String oldUsername;
    @Length(min = 8, max = 20)
    private String oldPassword;
    @NotBlank
    private String newUsername;
    @Length(min = 8, max = 20)
    private String newPassword;
    @Email
    private String newEmail;
    @Length(min = 9, max = 10)
    private String newPhone;
    @NotBlank
    private String newFirstName;
    @NotBlank
    private String newLastName;
    @NotBlank
    private String newPassportNumber;
    @NotBlank
    private Integer newNumberOfReservations;

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public String getNewPassportNumber() {
        return newPassportNumber;
    }

    public void setNewPassportNumber(String newPassportNumber) {
        this.newPassportNumber = newPassportNumber;
    }

    public Integer getNewNumberOfReservations() {
        return newNumberOfReservations;
    }

    public void setNewNumberOfReservations(Integer newNumberOfReservations) {
        this.newNumberOfReservations = newNumberOfReservations;
    }
}

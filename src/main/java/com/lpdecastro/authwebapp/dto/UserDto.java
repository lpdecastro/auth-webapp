package com.lpdecastro.authwebapp.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String salutation;
    private String title;
    private String gender;
    private String phone;
    private String mobile;
    private String password;
    private LocalDate birthday;

    private AddressDto address;

    private Boolean emailConsent;
    private Boolean smsConsent;
    private Boolean dataPrivacyConsent;

    private Boolean newsletterSubscription;
    private Boolean promoSubscription;
    private Boolean productUpdateSubscription;
    private Boolean eventNotificationSubscription;
    private Boolean surveySubscription;

    private List<ProfessionalInfoDto> professionalInfos = new ArrayList<>();
    private boolean professionalInfoTab;
}

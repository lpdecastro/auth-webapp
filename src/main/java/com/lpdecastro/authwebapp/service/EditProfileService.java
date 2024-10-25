package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.dto.AddressDto;
import com.lpdecastro.authwebapp.dto.ProfessionalInfoDto;
import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.entity.*;
import com.lpdecastro.authwebapp.repository.AddressRepository;
import com.lpdecastro.authwebapp.repository.ProfessionalInfoRepository;
import com.lpdecastro.authwebapp.repository.UserRepository;
import com.lpdecastro.authwebapp.util.LoginModelMapper;
import com.lpdecastro.authwebapp.util.SecurityUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EditProfileService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final LoginModelMapper loginModelMapper;
    private final AddressRepository addressRepository;
    private final ProfessionalInfoRepository professionalInfoRepository;

    public UserDto getCurrentUser() {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new IllegalStateException("No authenticated user found. Please log in.");
        }
        UserEntity userEntity = userRepository.findByEmail(email);
        UserDto userDto = loginModelMapper.convertEntityToDto(userEntity);

        for (Consent consent : userEntity.getConsents()) {
            switch (consent.getIdentifier()) {
                case "EMAIL":
                    userDto.setEmailConsent(consent.getOptIn());
                    break;
                case "SMS":
                    userDto.setSmsConsent(consent.getOptIn());
                    break;
                case "DATA_PRIVACY":
                    userDto.setDataPrivacyConsent(consent.getOptIn());
                    break;
            }
        }

        for (Subscription subscription : userEntity.getSubscriptions()) {
            switch (subscription.getIdentifier()) {
                case "NEWSLETTER":
                    userDto.setNewsletterSubscription(subscription.getOptIn());
                    break;
                case "PROMO":
                    userDto.setPromoSubscription(subscription.getOptIn());
                    break;
                case "PRODUCT":
                    userDto.setProductUpdateSubscription(subscription.getOptIn());
                    break;
                case "EVENT":
                    userDto.setEventNotificationSubscription(subscription.getOptIn());
                    break;
                case "SURVEY":
                    userDto.setSurveySubscription(subscription.getOptIn());
                    break;
            }
        }

        // Map professionalInfos from userEntity to userDto
        List<ProfessionalInfoDto> professionalInfoDtos = new ArrayList<>();
        for (ProfessionalInfo professionalInfo : userEntity.getProfessionalInfos()) {
            ProfessionalInfoDto dto = new ProfessionalInfoDto();
            dto.setTitle(professionalInfo.getTitle());
            dto.setCompany(professionalInfo.getCompany());
            dto.setIndustry(professionalInfo.getIndustry());
            dto.setYearsOfExperience(professionalInfo.getYearsOfExperience());
            professionalInfoDtos.add(dto);
        }
        userDto.setProfessionalInfos(professionalInfoDtos);  // Add to UserDto

        return userDto;
    }

    @Transactional
    public void editProfile(UserDto userDto) throws MessagingException, IOException {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new IllegalStateException("No authenticated user found. Please log in.");
        }

        UserEntity userEntity = userRepository.findByEmail(email);

        handleEmailChange(userDto, userEntity);
        updateBasicFields(userDto, userEntity);
        updateAddress(userDto.getAddress(), userEntity);
        updateConsents(userDto, userEntity);
        updateSubscriptions(userDto, userEntity);
        updateProfessions(userDto, userEntity);

        userRepository.save(userEntity);
    }

    private void updateProfessions(UserDto userDto, UserEntity userEntity) {
        if (!userDto.isProfessionalInfoTab()) {
            return;
        }
        // Delete existing professional info in batch from the database
        if (userEntity.getProfessionalInfos() != null && !userEntity.getProfessionalInfos().isEmpty()) {
            professionalInfoRepository.deleteAllInBatch(userEntity.getProfessionalInfos()); // Batch delete to improve performance
        }

        // Clear the in-memory collection after database deletion
        userEntity.getProfessionalInfos().clear();

        // Add the new professional info
        if (userDto.getProfessionalInfos() != null && !userDto.getProfessionalInfos().isEmpty()) {
            List<ProfessionalInfo> newProfessionalInfos = userDto.getProfessionalInfos().stream()
                    .map(dto -> {
                        ProfessionalInfo info = new ProfessionalInfo();
                        info.setTitle(dto.getTitle());
                        info.setCompany(dto.getCompany());
                        info.setIndustry(dto.getIndustry());
                        info.setYearsOfExperience(dto.getYearsOfExperience());
                        info.setUser(userEntity);  // Set the owning side
                        return info;
                    })
                    .toList();

            // Update both in-memory collection and persist in the database
            userEntity.getProfessionalInfos().addAll(newProfessionalInfos);
        }
    }

    private void handleEmailChange(UserDto userDto, UserEntity userEntity) throws MessagingException, IOException {
        String currentEmail = userEntity.getEmail();
        String newEmail = userDto.getEmail();

        if (newEmail != null && !newEmail.equals(currentEmail)) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new IllegalArgumentException("Email is already in use.");
            }

            String token = RandomString.make(25);
            userEntity.setEmail(newEmail);
            userEntity.setEmailVerifiedDate(null);
            userEntity.setEmailVerificationToken(token);
            userEntity.setEmailVerificationTokenDate(LocalDateTime.now());

            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String emailVerificationLink = baseUrl + "/verify-email?token=" + token;
            emailService.sendEmailChangedVerificationEmail(newEmail, userEntity.getFirstName(), emailVerificationLink);

            SecurityUtils.updateCurrentUsername(newEmail);
        }
    }

    private void updateBasicFields(UserDto userDto, UserEntity userEntity) {
        if (userDto.getFirstName() != null) userEntity.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) userEntity.setLastName(userDto.getLastName());
        if (userDto.getPhone() != null) userEntity.setPhone(userDto.getPhone());
        if (userDto.getMobile() != null) userEntity.setMobile(userDto.getMobile());
        if (userDto.getBirthday() != null) userEntity.setBirthday(userDto.getBirthday());
        if (userDto.getGender() != null) userEntity.setGender(userDto.getGender());
        if (userDto.getTitle() != null) userEntity.setTitle(userDto.getTitle());
        if (userDto.getSalutation() != null) userEntity.setSalutation(userDto.getSalutation());
        if (userDto.getMiddleName() != null) userEntity.setMiddleName(userDto.getMiddleName());
    }

    private void updateAddress(AddressDto addressDto, UserEntity userEntity) {
        if (addressDto == null) return;

        Address address = Optional.ofNullable(userEntity.getAddress()).orElseGet(Address::new);
        address.setUser(userEntity);

        if (addressDto.getAddress1() != null) address.setAddress1(addressDto.getAddress1());
        if (addressDto.getAddress2() != null) address.setAddress2(addressDto.getAddress2());
        if (addressDto.getCity() != null) address.setCity(addressDto.getCity());
        if (addressDto.getProvince() != null) address.setProvince(addressDto.getProvince());
        if (addressDto.getZip() != null) address.setZip(addressDto.getZip());
        if (addressDto.getCountry() != null) address.setCountry(addressDto.getCountry());

        userEntity.setAddress(address);
        addressRepository.save(address);
    }

    private void updateConsents(UserDto userDto, UserEntity userEntity) {
        Map<String, Boolean> consentMap = new HashMap<>();

        if (userDto.getEmailConsent() != null) {
            consentMap.put("EMAIL", userDto.getEmailConsent());
        }
        if (userDto.getSmsConsent() != null) {
            consentMap.put("SMS", userDto.getSmsConsent());
        }
        if (userDto.getDataPrivacyConsent() != null) {
            consentMap.put("DATA_PRIVACY", userDto.getDataPrivacyConsent());
        }

        consentMap.forEach((identifier, optIn) ->
                updateConsent(userEntity, identifier, optIn)
        );
    }

    private void updateSubscriptions(UserDto userDto, UserEntity userEntity) {
        Map<String, Boolean> subscriptionMap = new HashMap<>();

        if (userDto.getNewsletterSubscription() != null) {
            subscriptionMap.put("NEWSLETTER", userDto.getNewsletterSubscription());
        }
        if (userDto.getPromoSubscription() != null) {
            subscriptionMap.put("PROMO", userDto.getPromoSubscription());
        }
        if (userDto.getProductUpdateSubscription() != null) {
            subscriptionMap.put("PRODUCT", userDto.getProductUpdateSubscription());
        }
        if (userDto.getEventNotificationSubscription() != null) {
            subscriptionMap.put("EVENT", userDto.getEventNotificationSubscription());
        }
        if (userDto.getSurveySubscription() != null) {
            subscriptionMap.put("SURVEY", userDto.getSurveySubscription());
        }

        subscriptionMap.forEach((identifier, optIn) ->
                updateSubscription(userEntity, identifier, optIn)
        );
    }

    private void updateSubscription(UserEntity userEntity, String identifier, boolean optIn) {
        Optional<Subscription> existingSubscription = userEntity.getSubscriptions().stream()
                .filter(s -> s.getIdentifier().equals(identifier))
                .findFirst();

        if (existingSubscription.isPresent()) {
            Subscription subscription = existingSubscription.get();
            subscription.setOptIn(optIn);
            subscription.setOptInDate(LocalDateTime.now());
        } else {
            Subscription subscription = new Subscription();
            subscription.setUser(userEntity);
            subscription.setIdentifier(identifier);
            subscription.setOptIn(optIn);
            subscription.setOptInDate(LocalDateTime.now());
            subscription.setName(null);
            subscription.setChannel(null);
            userEntity.getSubscriptions().add(subscription);
        }
    }

    private void updateConsent(UserEntity userEntity, String identifier, Boolean optIn) {
        Optional<Consent> existingConsent = userEntity.getConsents().stream()
                .filter(c -> c.getIdentifier().equals(identifier))
                .findFirst();

        if (existingConsent.isPresent()) {
            Consent consent = existingConsent.get();
            consent.setOptIn(optIn);
            consent.setOptInDate(LocalDateTime.now());
        } else {
            Consent consent = new Consent();
            consent.setUser(userEntity);
            consent.setIdentifier(identifier);
            consent.setOptIn(optIn);
            consent.setOptInDate(LocalDateTime.now());
            userEntity.getConsents().add(consent);
        }
    }
}

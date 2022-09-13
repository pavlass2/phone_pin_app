package com.example.demo.phonenumber.service;

import com.example.demo.exception.ApiRequestException;
import com.example.demo.phonenumber.PhoneNumberConstants;
import com.example.demo.phonenumber.jpamodel.PhoneNumber;
import com.example.demo.phonenumber.repository.PhoneNumberRepository;
import com.example.demo.phonenumber.requestmodel.PinChangeRequest;
import com.example.demo.phonenumber.requestmodel.ValidatePinRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PhoneNumberService {
    private final PhoneNumberRepository phoneNumberRepository;
    private final PasswordEncoder pinEncoder;

    @Autowired
    public PhoneNumberService(PhoneNumberRepository phoneNumberRepository, PasswordEncoder pinEncoder) {
        this.phoneNumberRepository = phoneNumberRepository;
        this.pinEncoder = pinEncoder;
    }

    public PhoneNumber matchPhoneNumber(String prefix, String phoneNumber) {
        return phoneNumberRepository.matchPhoneNumber(
                prefix,
                phoneNumber
        );
    }

    public boolean isPinValid(ValidatePinRequest validatePinRequest) {
        PhoneNumber foundPhoneNumber = matchPhoneNumber(
                validatePinRequest.prefix(),
                validatePinRequest.phoneNumber());
        if (foundPhoneNumber == null) {
            return false;
        }

        return isPinValid(foundPhoneNumber, validatePinRequest.pin());
    }
    private boolean isPinValid(PhoneNumber phoneNumber, String pin) {
        log.info("Validating PIN for phoneNumber {} {}", phoneNumber.getPrefix(), phoneNumber.getPhoneNumber());
        boolean isValid = pinEncoder.matches(pin, phoneNumber.getEncryptedPin());

        log.info(
                "PIN for phoneNumber {} {} is {}",
                phoneNumber.getPrefix(),
                phoneNumber.getPhoneNumber(),
                isValid ? "valid" : "invalid"
        );
        return isValid;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumberRepository.findAll();
    }

    public PhoneNumber changePin(PinChangeRequest pinChangeRequest) {
        log.info("PIN change process started for number {} {}", pinChangeRequest.prefix(), pinChangeRequest.phoneNumber());
        PhoneNumber phoneNumber = phoneNumberRepository.matchPhoneNumber(
                pinChangeRequest.prefix(),
                pinChangeRequest.phoneNumber()
        );
        validatePinChangeRequest(phoneNumber, pinChangeRequest.oldPin(), pinChangeRequest.newPin());

        phoneNumber.setEncryptedPin(pinEncoder.encode(pinChangeRequest.newPin()));
        phoneNumberRepository.save(phoneNumber);

        log.info("PIN change for number {} {} completed successfully, new PIN has been set.", phoneNumber.getPrefix(), phoneNumber.getPhoneNumber());
        return phoneNumber;
    }

    private void validatePinChangeRequest(PhoneNumber phoneNumber, String oldPin, String newPin) {
        log.info("Validating PIN for number {} {}", phoneNumber.getPrefix(), phoneNumber.getPhoneNumber());

        if (phoneNumber == null) {
            ApiRequestException pinInvalidException = new ApiRequestException(PhoneNumberConstants.PIN_CHANGE_PHONE_NUMBER_NOT_FOUND);
            log.error(PhoneNumberConstants.PIN_CHANGE_PHONE_NUMBER_NOT_FOUND, pinInvalidException);
            throw pinInvalidException;
        }
        if (!isPinValid(phoneNumber, oldPin)) {
            ApiRequestException pinInvalidException = new ApiRequestException(PhoneNumberConstants.PIN_CHANGE_OLD_PIN_NOK);
            log.error(PhoneNumberConstants.PIN_CHANGE_OLD_PIN_NOK, pinInvalidException);
            throw pinInvalidException;
        }
        if (newPin.equals(oldPin)) {
            ApiRequestException pinInvalidException = new ApiRequestException(PhoneNumberConstants.PIN_CHANGE_NEW_PIN_EQUALS_OLD_PIN);
            log.error(PhoneNumberConstants.PIN_CHANGE_NEW_PIN_EQUALS_OLD_PIN, pinInvalidException);
            throw pinInvalidException;
        }

        log.info("PIN for number {} {} successfully validated.", phoneNumber.getPrefix(), phoneNumber.getPhoneNumber());
    }
}

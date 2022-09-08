package com.example.demo.phonenumber.controller;

import com.example.demo.phonenumber.PhoneNumberConstants;
import com.example.demo.phonenumber.jpamodel.PhoneNumber;
import com.example.demo.phonenumber.requestmodel.PinChangeRequest;
import com.example.demo.phonenumber.requestmodel.ValidatePinRequest;
import com.example.demo.phonenumber.service.PhoneNumberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "api/v1/phonenumbers")
@AllArgsConstructor
@Slf4j
public class PhoneNumberController {
    private final PhoneNumberService phoneNumberService;

    @GetMapping
    public List<PhoneNumber> getPhoneNumbers() {
        log.info("Get PhoneNumbers request received.");
        return phoneNumberService.getPhoneNumbers();
    }

    @GetMapping(path = "/pin")
    public String validatePin(@RequestBody ValidatePinRequest validatePinRequest) {
        log.info(
                "Validate PIN request received for number {} {}.",
                validatePinRequest.prefix(),
                validatePinRequest.phoneNumber()
        );
        return phoneNumberService.isPinValid(validatePinRequest) ?
                PhoneNumberConstants.PIN_VALIDATION_OK : PhoneNumberConstants.PIN_VALIDATION_NOK;
    }

    @PutMapping(path = "/pin")
    public PhoneNumber changePin(@RequestBody PinChangeRequest pinChangeRequest) {
        log.info(
                "Change PIN request received for number {} {}.",
                pinChangeRequest.prefix(),
                pinChangeRequest.phoneNumber());
        return phoneNumberService.changePin(pinChangeRequest);
    }
}

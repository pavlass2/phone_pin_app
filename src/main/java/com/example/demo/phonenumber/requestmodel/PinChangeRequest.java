package com.example.demo.phonenumber.requestmodel;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import static com.example.demo.phonenumber.PhoneNumberConstants.*;

public record PinChangeRequest(
        @Size(min = 2, max = 4, message = VALIDATION_ERROR_PREFIX)
        String prefix,

        @Size(min = 5, max = 13, message = VALIDATION_ERROR_PHONE_NUMBER)
        @Digits(integer = 13, fraction = 0, message = VALIDATION_ERROR_PHONE_NUMBER)
        String phoneNumber,

        @Digits(integer = 4, fraction = 0, message = "new " + VALIDATION_ERROR_PIN)
        @Size(min = 4, max = 4, message = "new " + VALIDATION_ERROR_PIN)
        String oldPin,

        @Digits(integer = 4, fraction = 0, message = "old " + VALIDATION_ERROR_PIN)
        @Size(min = 4, max = 4, message = "old " + VALIDATION_ERROR_PIN)
        String newPin
) {
}

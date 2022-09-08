package com.example.demo.phonenumber.requestmodel;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

public record ValidatePinRequest(
        @Size(min = 2, max = 4) String prefix,
        @Size(min = 5, max = 13) String phoneNumber,
        @Digits(integer = 4, fraction = 0) @Size(min = 4, max = 4) String pin) {
}

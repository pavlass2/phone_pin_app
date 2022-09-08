package com.example.demo.phonenumber.requestmodel;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

public record PinChangeRequest(
        @Size(min = 2, max = 4) String prefix,
        @Size(min = 5, max = 13) String phoneNumber,
        @Digits(integer = 4, fraction = 0) @Size(min = 4, max = 4) String oldPin,
        @Digits(integer = 4, fraction = 0) @Size(min = 4, max = 4) String newPin) {
}

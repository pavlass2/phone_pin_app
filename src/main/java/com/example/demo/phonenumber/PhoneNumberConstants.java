package com.example.demo.phonenumber;

public interface PhoneNumberConstants {
    String PIN_VALIDATION_OK = "PIN je validní";
    String PIN_VALIDATION_NOK = "Nesprávné hodnoty";
    String PIN_CHANGE_PHONE_NUMBER_NOT_FOUND = "Phone number not found, PIN change not allowed";
    String PIN_CHANGE_OLD_PIN_NOK = "Current PIN is invalid, PIN change not allowed";
    String PIN_CHANGE_NEW_PIN_EQUALS_OLD_PIN = "New PIN is the same as old PIN, PIN change not allowed";
    String PIN_CHANGE_NEW_PIN_DOES_NOT_FULFILL_REQUIREMENTS = "PIN must have exactly 4 digits";
    String DEFAULT_PIN = "0000";
}

package com.example.demo;

import com.example.demo.exception.ApiRequestException;
import com.example.demo.phonenumber.PhoneNumberConstants;
import com.example.demo.phonenumber.controller.PhoneNumberController;
import com.example.demo.phonenumber.jpamodel.PhoneNumber;
import com.example.demo.phonenumber.repository.PhoneNumberRepository;
import com.example.demo.phonenumber.requestmodel.PinChangeRequest;
import com.example.demo.phonenumber.requestmodel.ValidatePinRequest;
import com.example.demo.phonenumber.service.PhoneNumberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoApplicationTests {
	private PhoneNumberController phoneNumberController;

	@Autowired
	private PhoneNumberRepository repository;

	private PasswordEncoder pinEncoder;

	private List<PhoneNumber> phoneNumbers;

	@BeforeAll
	public void init() {
		pinEncoder = new BCryptPasswordEncoder();
		PhoneNumberService service = new PhoneNumberService(repository, pinEncoder);
		phoneNumberController = new PhoneNumberController(service);

		PhoneNumber zahora = new PhoneNumber();
		zahora.setOwnerName("Vlastislav Záhora");
		zahora.setPrefix("+421");
		zahora.setPhoneNumber("123456789");
		zahora.setEncryptedPin(pinEncoder.encode(PhoneNumberConstants.DEFAULT_PIN));

		PhoneNumber novy = new PhoneNumber();
		novy.setOwnerName("Gustav Novy");
		novy.setPrefix("+41");
		novy.setPhoneNumber("9876543210987");
		novy.setEncryptedPin(pinEncoder.encode(PhoneNumberConstants.DEFAULT_PIN));

		phoneNumbers = repository.saveAll(Arrays.asList(zahora, novy));
	}

	@Test
	void contextLoads() {
	}

	@Test
	void givenPhoneNumbersExist_whenGetPhoneNumbers_thenPhoneNumbersAreReturned() {
		List<PhoneNumber> returnedPhoneNumbers = phoneNumberController.getPhoneNumbers();
		assertThat(returnedPhoneNumbers, hasItems(phoneNumbers.get(0), phoneNumbers.get(1)));
	}

	@Test
	void givenCorrectPin_whenValidatePin_thenPinValidationIsSuccess() {
		String zahoraResultSuccess = phoneNumberController.validatePin(
				new ValidatePinRequest(
						phoneNumbers.get(0).getPrefix(),
						phoneNumbers.get(0).getPhoneNumber(),
						PhoneNumberConstants.DEFAULT_PIN
				)
		);
		assertThat(zahoraResultSuccess, is(PhoneNumberConstants.PIN_VALIDATION_OK));

		String novyResultSuccess = phoneNumberController.validatePin(
				new ValidatePinRequest(
						phoneNumbers.get(1).getPrefix(),
						phoneNumbers.get(1).getPhoneNumber(),
						PhoneNumberConstants.DEFAULT_PIN
				)
		);
		assertThat(novyResultSuccess, is(PhoneNumberConstants.PIN_VALIDATION_OK));
	}

	@Test
	void givenWrongPin_whenValidatePin_thenPinValidationIsFailure() {
		String wrongPin = "0001";
		String zahoraResultFailure = phoneNumberController.validatePin(
				new ValidatePinRequest(
						phoneNumbers.get(0).getPrefix(),
						phoneNumbers.get(0).getPhoneNumber(),
						wrongPin
				)
		);
		assertThat(zahoraResultFailure, is(PhoneNumberConstants.PIN_VALIDATION_NOK));
	}

	@Test
	void givenCorrectPin_whenChangePin_thenPinIsChanged() {
		String newPin = "0001";
		PhoneNumber testovic = new PhoneNumber();
		testovic.setOwnerName("Test Testovic");
		testovic.setPrefix("+410");
		testovic.setPhoneNumber("98765410987");
		testovic.setEncryptedPin(pinEncoder.encode(PhoneNumberConstants.DEFAULT_PIN));
		repository.save(testovic);

		PhoneNumber zahora = phoneNumberController.changePin(
				new PinChangeRequest(
						testovic.getPrefix(),
						testovic.getPhoneNumber(),
						PhoneNumberConstants.DEFAULT_PIN,
						newPin
				)
		);
		String zahoraResultSuccess = phoneNumberController.validatePin(
				new ValidatePinRequest(
						zahora.getPrefix(),
						zahora.getPhoneNumber(),
						newPin
				)
		);
		assertThat(zahoraResultSuccess, is(PhoneNumberConstants.PIN_VALIDATION_OK));
	}

	@Test
	void givenWrongOldPin_whenChangePin_thenPinIsNotChanged() {
		String wrongOldPin = "0001";
		String newPin = "9999";

		ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
				phoneNumberController.changePin(
					new PinChangeRequest(
							phoneNumbers.get(0).getPrefix(),
							phoneNumbers.get(0).getPhoneNumber(),
							wrongOldPin,
							newPin
					)
				)
		);

		assertThat(exception.getMessage(), is(PhoneNumberConstants.PIN_CHANGE_OLD_PIN_NOK));
	}
}

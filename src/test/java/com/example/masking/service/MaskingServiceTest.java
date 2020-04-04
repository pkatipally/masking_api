package com.example.masking.service;

import com.example.masking.enums.MaskingInput;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MaskingServiceTest {

    private MaskingService maskingService;

    @BeforeAll
    void setup(){
        maskingService =  new MaskingService();
    }

    @Test
    void testGetMaskedResultEmail() throws Exception {
        String expectedResult = "a***e@gmail.com";
        String input  = "abcde@gmail.com";
        MaskingService maskingServiceSpy = Mockito.spy(this.maskingService);

        when(maskingServiceSpy.getMaskedEmail(input)).thenReturn(expectedResult);
        String actualResult = maskingServiceSpy.getMaskedResult(MaskingInput.EMAIL, input);

        assertThat(actualResult, is(expectedResult));
        verify(maskingServiceSpy, timeout(1)).getMaskedEmail(input);
        Mockito.reset(maskingServiceSpy);

    }

    @Test
    void testGetMaskedResultEmailException() throws Exception {
        String input  = "abcde";
        MaskingService maskingServiceSpy = Mockito.spy(this.maskingService);

        doThrow(new Exception("Invalid  Email")).when(maskingServiceSpy).getMaskedEmail(input);

        assertThrows(Exception.class, () -> maskingServiceSpy.getMaskedResult(MaskingInput.EMAIL, input));
        verify(maskingServiceSpy, timeout(1)).getMaskedEmail(input);
        Mockito.reset(maskingServiceSpy);
    }

    @Test
    void testGetMaskedResultPhoneNumberException() throws Exception {
        String input  = "1234";
        MaskingService maskingServiceSpy = Mockito.spy(this.maskingService);

        doThrow(new Exception("Invalid  phone number")).when(maskingServiceSpy).getMaskedPhoneNumber(input);

        assertThrows(Exception.class, () -> maskingServiceSpy.getMaskedResult(MaskingInput.PHONE, input));
        verify(maskingServiceSpy, timeout(1)).getMaskedPhoneNumber(input);
        Mockito.reset(maskingServiceSpy);
    }

    @Test
    void testGetMaskedResultPhone() throws Exception {
        String expectedResult = "123***7890";
        String input  = "1234567890";
        MaskingService maskingServiceSpy = Mockito.spy(this.maskingService);

        when(maskingServiceSpy.getMaskedPhoneNumber(input)).thenReturn(expectedResult);
        String actualResult = maskingServiceSpy.getMaskedResult(MaskingInput.PHONE, input);

        assertThat(actualResult, is(expectedResult));
        verify(maskingServiceSpy, timeout(1)).getMaskedPhoneNumber(input);
        Mockito.reset(maskingServiceSpy);

    }

    @ParameterizedTest
    @MethodSource("validEmailMaskProvider")
    void testGetCustomerEmailSuccess(String input, String expectedOutput) throws Exception {
        assertThat(maskingService.getMaskedEmail(input), is(expectedOutput));

    }

    @ParameterizedTest
    @MethodSource("invalidEmailMaskProvider")
    void testGetCustomerEmailException(String input) throws Exception {
        assertThrows(Exception.class, () -> maskingService.getMaskedEmail(input));
    }

    @ParameterizedTest
    @MethodSource("validPhoneNumberMaskProvider")
    void testGetCustomerPhoneNumberSuccess(String input, String expectedOutput) throws Exception {
        assertThat(maskingService.getMaskedPhoneNumber(input), is(expectedOutput));

    }

    @ParameterizedTest
    @MethodSource("invalidPhoneNumberMaskProvider")
    void testGetCustomerPhoneNumberException(String input) throws Exception {
        assertThrows(Exception.class, () -> maskingService.getMaskedPhoneNumber(input));
    }

    private static Stream<Arguments> validEmailMaskProvider() {
        return Stream.of(
                arguments("abcd@gmail.com", "a**d@gmail.com"),
                arguments("abcdefgh@yahoo.com","a******h@yahoo.com"),
                arguments("ab@gmail.com", "ab@gmail.com")
        );
    }

    private static Stream<Arguments> invalidEmailMaskProvider() {
        return Stream.of(
                arguments("abcd@gmail"),
                arguments("abcdefghyahoo.com"),
                arguments("null"),
                arguments("undefined")
        );
    }

    private static Stream<Arguments> validPhoneNumberMaskProvider() {
        return Stream.of(
                arguments("1234567890", "123***7890"),
                arguments("123-456-7890","123-***-7890"),
                arguments("123.456.7890", "123.***.7890")
        );
    }

    private static Stream<Arguments> invalidPhoneNumberMaskProvider() {
        return Stream.of(
                arguments("123"),
                arguments("123423243-42343"),
                arguments("null"),
                arguments("undefined")
        );
    }
}

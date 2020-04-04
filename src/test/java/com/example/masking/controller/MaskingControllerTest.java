package com.example.masking.controller;

import com.example.masking.enums.MaskingInput;
import com.example.masking.service.MaskingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MaskingControllerTest {
    @InjectMocks
    MaskingController maskingController;

    @Mock
    MaskingService maskingService;

    @BeforeAll
    void setup(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void getMaskedResultEmailSuccess() throws Exception {
        String input = "priyareddy@gmail.com";
        String expected = "p********d@gmail.com";
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(maskingService.getMaskedResult(MaskingInput.EMAIL, input)).thenReturn(expected);
        ResponseEntity<String> actualResponse = maskingController.getMaskedResult(MaskingInput.EMAIL, input);

        verify(maskingService, times(1)).getMaskedResult(MaskingInput.EMAIL, input);
        assertThat(actualResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
        assertThat(actualResponse.getBody(), is(expected));
    }

    @Test
    public void getMaskedResultEmailException() throws Exception {
        String input = "priyar";
        doThrow(new Exception("Invalid input")).when(maskingService).getMaskedResult(MaskingInput.EMAIL, input);
        ResponseEntity<String> expectedResponse = new ResponseEntity<String>(HttpStatus.UNPROCESSABLE_ENTITY);
        ResponseEntity<String> actualResponse = maskingController.getMaskedResult(MaskingInput.EMAIL, input);

        verify(maskingService, times(1)).getMaskedResult(MaskingInput.EMAIL, input);
        assertThat(actualResponse.getStatusCode(), is(expectedResponse.getStatusCode()));

    }

}

package com.example.masking.service;

import com.example.masking.enums.MaskingInput;
import org.springframework.stereotype.Service;

@Service
public class MaskingService {

    private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final String PHONE_NUMBER_PATTERN = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";


    public String getMaskedResult(MaskingInput type, String input ) throws Exception {
        switch (type){
            case EMAIL:
                return getMaskedEmail(input);
            case PHONE:
                return getMaskedPhoneNumber(input);
            default:
                //do nothing
        }
        return input;
    }

    protected String getMaskedEmail(String email) throws Exception {
        if(!email.matches(EMAIL_PATTERN)) throw new Exception("Invalid email");
        else return email.replaceAll("(?<=.{1}).(?=[^@]+@)", "*");
    }

    protected String getMaskedPhoneNumber(String phoneNumber) throws Exception{
        if(!phoneNumber.matches(PHONE_NUMBER_PATTERN)) throw new Exception("Invalid phone number");
        else {
            StringBuilder phoneSb = new StringBuilder(phoneNumber);
            if(phoneNumber.length() == 12){
                return phoneSb.replace(4,7, "***").toString();
            }else{
                return  phoneSb.replace(3,6, "***").toString();
            }
        }
    }
}


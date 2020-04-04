package com.example.masking.controller;

import com.example.masking.enums.MaskingInput;
import com.example.masking.service.MaskingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mask")
public class MaskingController {

    private MaskingService maskingService;

    public MaskingController(MaskingService maskingService){
        this.maskingService = maskingService;
    }

    @GetMapping("/{type}/{input}")
    public ResponseEntity<String> getMaskedResult(@PathVariable MaskingInput type, @PathVariable String input){
        try {
            String result = maskingService.getMaskedResult(type, input);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}

package com.sample.nicepay.api;

import com.sample.nicepay.dto.NicepayFormInDto;
import com.sample.nicepay.dto.NicepayFormOutDto;
import com.sample.nicepay.service.NicepayService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api")
public class NicepayApi {
    @Autowired
    NicepayService service;

    @GetMapping("payFormInit")
    public ResponseEntity<Object> payFormInit(){
        HttpStatus returnStatus = HttpStatus.OK;
        NicepayFormOutDto outDto = service.payFormInit();
        return ResponseEntity.status(returnStatus).body(outDto);
    }


//    @GetMapping("/payFormInit")
//    public ResponseEntity<Object> payFormInit(@ModelAttribute NicepayFormInDto inDto){
//        HttpStatus returnStatus = HttpStatus.OK;
//        NicepayFormOutDto outDto = service.payFormInit(inDto);
//        return ResponseEntity.status(returnStatus).body(outDto);
//    }

//    @PostMapping("/callback")
//    public ResponseEntity<Object> createMember(@RequestBody SampleInDto inDto){
//        return ResponseEntity.status(returnStatus).body(response);
//    }

}

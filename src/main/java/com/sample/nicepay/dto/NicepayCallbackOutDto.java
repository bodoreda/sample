package com.sample.nicepay.dto;

import lombok.Data;

@Data
public class NicepayCallbackOutDto {

    private String resultCode;

    private String resultMsg;

    private String payMethod;

    private String goodsName;

    private String amt;

    private String tid;

    private String signature;

    private String paySignature;
}

package com.sample.nicepay.dto;

import lombok.Data;

@Data
public class NicepayCallbackInDto {

    private String authResultCode;

    private String authResultMsg;

    private String nextAppUrl;

    private String txTid;

    private String authToken;

    private String payMethod;

    private String mid;

    private String moid;

    private String amt;

    private String reqReserved;

    private String netCancelURL;

    private String signature;
}

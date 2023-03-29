package com.sample.nicepay.dto;

import lombok.Data;

@Data
public class NicepayFormOutDto {

    private String goodsName;
    private String price;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;
    private String moid;
    private String returnURL;
    private String ediDate;
    private String signData;
    private String mid;

}

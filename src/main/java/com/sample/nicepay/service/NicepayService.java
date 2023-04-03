package com.sample.nicepay.service;

import com.sample.nicepay.dto.NicepayCallbackInDto;
import com.sample.nicepay.dto.NicepayCallbackOutDto;
import com.sample.nicepay.dto.NicepayFormOutDto;
import com.sample.nicepay.util.DataEncrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.sample.nicepay.util.NicepayUtil.*;

@Service
public class NicepayService {
    public NicepayFormOutDto payFormInit() {
        /* 추후 inDto에서 민감정보를 제외한 일부 data를 전송받는 방향으로 변경 */
        
        /*
         *******************************************************
         * <결제요청 파라미터>
         * 결제시 Form 에 보내는 결제요청 파라미터입니다.
         * 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며,
         * 추가 가능한 옵션 파라미터는 연동메뉴얼을 참고하세요.
         *******************************************************
         */
        String merchantKey 		= "EYzu8jGGMfqaDEp76gSckuvnaHHu+bC4opsSN6lHv3b2lurNYkVXrZ7Z1AoqQnXI3eLuaUFyoRNC6FkrzVjceg=="; // 상점키
        String merchantID 		= "nicepay00m"; 				// 상점아이디
        String goodsName 		= "테스트상품명"; 				// 결제상품명
        String price 			= "1004"; 						// 결제상품금액
        String buyerName 		= "테스트구매자명"; 				// 구매자명
        String buyerTel 		= "01000000000"; 				// 구매자연락처
        String buyerEmail 		= "happy@day.co.kr"; 			// 구매자메일주소
        String moid 			= "moid1234567890"; 			// 상품주문번호
        String returnURL 		= "http://localhost:8080/NicePayResult"; // 결과페이지(절대경로) - 모바일 결제창 전용

        /*
         *******************************************************
         * <해쉬암호화> (수정하지 마세요)
         * SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
         *******************************************************
         */
        DataEncrypt sha256Enc 	= new DataEncrypt();
        String ediDate 			= getyyyyMMddHHmmss();
        String signData 		= sha256Enc.encrypt(ediDate + merchantID + price + merchantKey);

        NicepayFormOutDto outDto = new NicepayFormOutDto();
        outDto.setGoodsName(goodsName);
        outDto.setPrice(price);
        outDto.setBuyerName(buyerName);
        outDto.setBuyerTel(buyerTel);
        outDto.setBuyerEmail(buyerEmail);
        outDto.setMoid(moid);
        outDto.setReturnURL(returnURL);
        outDto.setEdiDate(ediDate);
        outDto.setSignData(signData);
        outDto.setMid(merchantID);

        return outDto;
    }


    public NicepayCallbackOutDto callback(NicepayCallbackInDto inDto) throws Exception {
        /*
         ****************************************************************************************
         * <인증 결과 파라미터>
         ****************************************************************************************
         */
        String authResultCode 	= inDto.getAuthResultCode(); 	// 인증결과 : 0000(성공)
        String authResultMsg 	= inDto.getAuthResultMsg(); 	// 인증결과 메시지
        String nextAppURL 		= inDto.getNextAppUrl(); 		// 승인 요청 URL
        String txTid 			= inDto.getTxTid(); 			// 거래 ID
        String authToken 		= inDto.getAuthToken(); 		// 인증 TOKEN
        String payMethod 		= inDto.getPayMethod(); 		// 결제수단
        String mid 				= inDto.getMid(); 				// 상점 아이디
        String moid 			= inDto.getMoid(); 			    // 상점 주문번호
        String amt 				= inDto.getAmt(); 				// 결제 금액
        String reqReserved 		= inDto.getReqReserved(); 		// 상점 예약필드
        String netCancelURL 	= inDto.getNetCancelURL();  	// 망취소 요청 URL
//        String authSignature    = inDto.getSignature();			// Nicepay에서 내려준 응답값의 무결성 검증 Data

        /*
         ****************************************************************************************
         * Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한 요소를 방지하기 위해 연동 시 사용하시기 바라며
         * 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
         ****************************************************************************************
         */
        DataEncrypt sha256Enc 	= new DataEncrypt();
        String merchantKey 		= "EYzu8jGGMfqaDEp76gSckuvnaHHu+bC4opsSN6lHv3b2lurNYkVXrZ7Z1AoqQnXI3eLuaUFyoRNC6FkrzVjceg=="; // 상점키

//인증 응답 Signature = hex(sha256(AuthToken + MID + Amt + MerchantKey)
//String authComparisonSignature = sha256Enc.encrypt(authToken + mid + amt + merchantKey);

        /*
         ****************************************************************************************
         * <승인 결과 파라미터 정의>
         * 샘플페이지에서는 승인 결과 파라미터 중 일부만 예시되어 있으며,
         * 추가적으로 사용하실 파라미터는 연동메뉴얼을 참고하세요.
         ****************************************************************************************
         */
        String ResultCode 	= ""; String ResultMsg 	= ""; String PayMethod 	= "";
        String GoodsName 	= ""; String Amt 		= ""; String TID 		= "";
//String Signature = ""; String paySignature = "";


        /*
         ****************************************************************************************
         * <인증 결과 성공시 승인 진행>
         ****************************************************************************************
         */
        String resultJsonStr = "";
        if(authResultCode.equals("0000") /*&& authSignature.equals(authComparisonSignature)*/){
            /*
             ****************************************************************************************
             * <해쉬암호화> (수정하지 마세요)
             * SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
             ****************************************************************************************
             */
            String ediDate			= getyyyyMMddHHmmss();
            String signData 		= sha256Enc.encrypt(authToken + mid + amt + ediDate + merchantKey);

            /*
             ****************************************************************************************
             * <승인 요청>
             * 승인에 필요한 데이터 생성 후 server to server 통신을 통해 승인 처리 합니다.
             ****************************************************************************************
             */
            StringBuffer requestData = new StringBuffer();
            requestData.append("TID=").append(txTid).append("&");
            requestData.append("AuthToken=").append(authToken).append("&");
            requestData.append("MID=").append(mid).append("&");
            requestData.append("Amt=").append(amt).append("&");
            requestData.append("EdiDate=").append(ediDate).append("&");
            requestData.append("CharSet=").append("utf-8").append("&");
            requestData.append("SignData=").append(signData);

            resultJsonStr = connectToServer(requestData.toString(), nextAppURL);

            HashMap resultData = new HashMap();
            boolean paySuccess = false;
            if("9999".equals(resultJsonStr)){
                /*
                 *************************************************************************************
                 * <망취소 요청>
                 * 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.
                 *************************************************************************************
                 */
                StringBuffer netCancelData = new StringBuffer();
                requestData.append("&").append("NetCancel=").append("1");
                String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL);

                HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
                ResultCode = (String)cancelResultData.get("ResultCode");
                ResultMsg = (String)cancelResultData.get("ResultMsg");
		/*Signature = (String)cancelResultData.get("Signature");
		String CancelAmt = (String)cancelResultData.get("CancelAmt");
		paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);*/
            }else{
                resultData = jsonStringToHashMap(resultJsonStr);
                ResultCode 	= (String)resultData.get("ResultCode");	// 결과코드 (정상 결과코드:3001)
                ResultMsg 	= (String)resultData.get("ResultMsg");	// 결과메시지
                PayMethod 	= (String)resultData.get("PayMethod");	// 결제수단
                GoodsName   = (String)resultData.get("GoodsName");	// 상품명
                Amt       	= (String)resultData.get("Amt");		// 결제 금액
                TID       	= (String)resultData.get("TID");		// 거래번호
                // Signature : Nicepay에서 내려준 응답값의 무결성 검증 Data
                // 가맹점에서 무결성을 검증하는 로직을 구현하여야 합니다.
		/*Signature = (String)resultData.get("Signature");
		paySignature = sha256Enc.encrypt(TID + mid + Amt + merchantKey);*/

                /*
                 *************************************************************************************
                 * <결제 성공 여부 확인>
                 *************************************************************************************
                 */
                if(PayMethod != null){
                    if(PayMethod.equals("CARD")){
                        if(ResultCode.equals("3001")) paySuccess = true; // 신용카드(정상 결과코드:3001)
                    }else if(PayMethod.equals("BANK")){
                        if(ResultCode.equals("4000")) paySuccess = true; // 계좌이체(정상 결과코드:4000)
                    }else if(PayMethod.equals("CELLPHONE")){
                        if(ResultCode.equals("A000")) paySuccess = true; // 휴대폰(정상 결과코드:A000)
                    }else if(PayMethod.equals("VBANK")){
                        if(ResultCode.equals("4100")) paySuccess = true; // 가상계좌(정상 결과코드:4100)
                    }else if(PayMethod.equals("SSG_BANK")){
                        if(ResultCode.equals("0000")) paySuccess = true; // SSG은행계좌(정상 결과코드:0000)
                    }else if(PayMethod.equals("CMS_BANK")){
                        if(ResultCode.equals("0000")) paySuccess = true; // 계좌간편결제(정상 결과코드:0000)
                    }
                }
            }
        }else/*if(authSignature.equals(authComparisonSignature))*/{
            ResultCode 	= authResultCode;
            ResultMsg 	= authResultMsg;
        }/*else{
            System.out.println("인증 응답 Signature : " + authSignature);
            System.out.println("인증 생성 Signature : " + authComparisonSignature);
        }*/

        NicepayCallbackOutDto outDto = new NicepayCallbackOutDto();
        outDto.setResultCode(ResultCode);
        outDto.setResultMsg(ResultMsg);
        outDto.setPayMethod(PayMethod);
        outDto.setGoodsName(GoodsName);
        outDto.setAmt(Amt);
        outDto.setTid(TID);

        return outDto;
    }
}

package com.example.demo.models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;



@Getter
@Builder
public class CardModel {

    /**
     * 20자리 카드번호
     */
    @NotNull
    private  String cardNo ;
    /**
     * 8자리 승인금액
     */
    @NotNull
    private  Integer amount ;
    /**
     * 8자리 부가세
     */
    @NotNull
    private  Integer vat ;

    public void Generate(byte[] bytes){
        cardNo = new String(bytes,0, 20,  StandardCharsets.UTF_8) ;
        amount = Integer.valueOf(new String(bytes,20, 8) );
        vat = Integer.valueOf(new String(bytes,28, 8) );
    }

    @Override
    public String toString() {

        return StringUtils.rightPad(cardNo, 20) +
                StringUtils.leftPad(String.valueOf(amount), 8, "0") +
                StringUtils.leftPad(String.valueOf(vat), 8, "0");
    }
}

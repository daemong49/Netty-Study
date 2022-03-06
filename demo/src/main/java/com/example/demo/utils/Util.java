package com.example.demo.utils;

import java.util.AbstractMap;
import java.util.Map;

public  class  Util {

    /**
     * 메시지 타입과 메시지 길이 반환
     * @param header
     * @return
     */
    public static Map.Entry<String, Integer> parsingMessageHeader(String header){
        Map.Entry<String, Integer> values = new AbstractMap.SimpleEntry<>("0000", 0) ;

        if(header.length() == 8){
            values = new AbstractMap.SimpleEntry<>(header.substring(0, 4), Integer.valueOf(header.substring(4))) ;
        }

        return values ;
    }


    public static int byteArrayToInt(byte[] b, int lengthDiv){
        int byteInt = 0;
        if (lengthDiv==2){
            byteInt = ((b[1] & 0xFF) << 8) | (b[0] & 0xFF);
        }else if (lengthDiv==4){
            byteInt = b[0] & 0xFF |
                    (b[1] & 0xFF) << 8 |
                    (b[2] & 0xFF) << 16 |
                    (b[3] & 0xFF) << 24;
        }
        return byteInt;
    }



}

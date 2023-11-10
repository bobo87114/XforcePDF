package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author Bobo
 * @create 2019-04-17 13:42
 * @since 1.0.0
 */
public class Base64Utils {
    public static String encode(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
//            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    public static String decoder(String s) {
        byte[] b;
        String result = null;
        if (s != null) {
//            Base64Decoder decoder = new Base64Decoder();
//            try {
//                b = decoder.decodeBuffer(s);
//                result = new String(b, "utf-8");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
        return result;
    }
}

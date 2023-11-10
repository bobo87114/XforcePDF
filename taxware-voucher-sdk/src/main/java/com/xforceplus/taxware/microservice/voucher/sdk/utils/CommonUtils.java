package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * 通用帮助类
 *
 * @author Bobo
 * @create 2019-08-20 16:33
 * @since 1.0.0
 */
public class CommonUtils {
    /**
     * 生成请求流水号
     *
     * @return
     */
    public static String getSerialNo() {
        String nowDate = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        String randomStr = "";
        return String.format("%s%s", nowDate, randomStr);
    }

}

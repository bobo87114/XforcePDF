package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bobo
 * @date 2017/7/18
 */
public class ListUtils {
    /**
     * 按指定大小，分隔集合，将集合按规定个数分为n个部分
     * @param list
     * @param len
     * @return
     */
    public static List<List<?>> splitList(List<?> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }
        int size = list.size();
        int count = (size + len - 1) / len;

        List<List<?>> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<?> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }
}

package com.zwn.trainserverspringboot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据时间生成随机订单号
 */
public class GenerateNum {
    /**
     * 全局自增数
     */

    private static int count = 0;

    /**
     *     每毫秒秒最多生成多少订单（最好是像9999这种准备进位的值）
     */
    private static final int total = 99;

    /**
     * 格式化的时间字符串
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 获取当前时间年月日时分秒毫秒字符串
     */
    private static String getNowDateStr() {
        return sdf.format(new Date());
    }

    /**
     * 记录上一次的时间，用来判断是否需要递增全局数
     */
    private static String now = null;

    /**
    *生成一个订单号
    */
    public static String generateOrder() {
        String dataStr = getNowDateStr();
        if (dataStr.equals(now)) {
            count++;// 自增
        } else {
            count = 1;
            now = dataStr;
        }
        // 算补位
        int countInteger = String.valueOf(total).length() - String.valueOf(count).length();
        //// 补字符串

        String bu = "0".repeat(Math.max(0, countInteger)) + count;
        if (count >= total) {
            count = 0;
        }

        return dataStr + bu;
    }

}

package com.zwn.trainserverspringboot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /*
     *  是否是中文字符串
     */
    public static boolean isChineseStr(String str){
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }


    public static boolean isMobileNum(String telNum) {
        Pattern p = Pattern.compile("[1][34578][0-9]{9}");
        Matcher m = p.matcher(telNum);
        return m.matches();
    }

    public static boolean isEmailFormat(String content){
        String REGEX="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,3}){1,3}$";
        Pattern p = Pattern.compile(REGEX);
        Matcher matcher=p.matcher(content);

        return matcher.matches();
    }
}

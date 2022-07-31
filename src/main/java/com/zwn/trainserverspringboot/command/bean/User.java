package com.zwn.trainserverspringboot.command.bean;

import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class User {
    long userId;
    String userName;
    boolean gender;
    String loginKey;
    String email;

    public ResultCodeEnum isRegisterLegal(){
        if (userName == null || userName.length() == 0 || loginKey == null  || loginKey.length() == 0 || email == null ){
            return ResultCodeEnum.BAD_REQUEST;
        }else if (!StringUtil.isMobileNum(String.valueOf(userId))){
            return ResultCodeEnum.REGISTER_ILLEGAL_PHONE;
        }else if (!StringUtil.isEmailFormat(email)){
            return ResultCodeEnum.REGISTER_ILLEGAL_EMAIL;
        } else if (loginKey.length() < 8) {
            return ResultCodeEnum.REGISTER_ILLEGAL_PASSWORD;
        }else {
            return ResultCodeEnum.SUCCESS;
        }
    }

    public boolean isGender() {
        return gender;
    }
}

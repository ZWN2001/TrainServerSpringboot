package com.zwn.trainserverspringboot.util;

public class UserCheck {
    Result check(){
        try {
            if (UserUtil.getCurrentUserId() == 0 ){
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }catch (Exception e){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        return Result.getResult(ResultCodeEnum.SUCCESS);
    }

    Result checkWithUserId(long userId){
        try {
            if (UserUtil.getCurrentUserId() == 0 || UserUtil.getCurrentUserId() != userId){
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }catch (Exception e){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        return Result.getResult(ResultCodeEnum.SUCCESS);
    }
}

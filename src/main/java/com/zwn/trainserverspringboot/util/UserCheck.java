package com.zwn.trainserverspringboot.util;

public class UserCheck {
    public static Result check(){
        try {
            if (UserUtil.getCurrentUserId() == 0 ){
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }catch (Exception e){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        return Result.getResult(ResultCodeEnum.SUCCESS);
    }

    public static Result checkWithUserId(long userId){
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

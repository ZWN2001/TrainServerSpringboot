package com.zwn.dbtext.util;

import lombok.*;
import lombok.experimental.Tolerate;

import java.io.Serializable;

@AllArgsConstructor
@ToString
public class Result implements Serializable {

    @NonNull
    @Getter
    @Setter
    private Integer code;

    @NonNull
    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private Object data;

    @Tolerate
    public Result() {
    }

    public static Result getResult(ResultCodeEnum resultCodeEnum) {
        return getResult(resultCodeEnum, null);
    }

    public static Result getResult(ResultCodeEnum resultCodeEnum, Object data) {
        int code = resultCodeEnum.code;
        String message = resultCodeEnum.message;
        return new Result(code, message, data);
    }

}
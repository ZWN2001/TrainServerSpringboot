package com.zwn.trainserverspringboot.exception;

import com.zwn.trainserverspringboot.util.Result;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

//	private static final long serialVersionUID = 3066235133493104130L;
	private final Result result;
	
	public CustomException(Result result) {
        this.result = result;
    }
}

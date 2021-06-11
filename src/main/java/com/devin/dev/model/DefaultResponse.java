package com.devin.dev.model;

import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class DefaultResponse<T> {

    private final int statusCode;
    private final ResponseMessage responseMessage;
    private final T data;

    public DefaultResponse(StatusCode statusCode, ResponseMessage responseMessage) {
        this.statusCode = statusCode.getCode();
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public DefaultResponse(StatusCode statusCode, ResponseMessage responseMessage, T data) {
        this.statusCode = statusCode.getCode();
        this.responseMessage = responseMessage;
        this.data = data;
    }
}

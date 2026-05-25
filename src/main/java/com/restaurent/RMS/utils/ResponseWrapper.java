package com.restaurent.RMS.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseWrapper<T> {
    private int statusCode;
    private String statusMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}

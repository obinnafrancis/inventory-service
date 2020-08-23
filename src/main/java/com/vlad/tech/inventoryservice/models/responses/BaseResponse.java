package com.vlad.tech.inventoryservice.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private String code;
    private String description;
    private Object responseData;
    private Object originalRequest;
    private List<ValidationError> errors;

    public BaseResponse(ResponseMapper responseMapper, Object data ){
        this.code = responseMapper.getCode();
        this.description = responseMapper.getDescription();
        this.responseData = data;
    }

    public BaseResponse(ResponseMapper responseMapper, List<ValidationError> errors){
        this.code = responseMapper.getCode();
        this.description = responseMapper.getDescription();
        this.errors = errors;
    }
}

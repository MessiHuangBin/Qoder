package com.example.springbootmybatis.advice;

import com.example.springbootmybatis.common.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应处理器，统一包装返回结果
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查返回类型是否已经是Result类型，如果是则不需要再次包装
        return !returnType.getParameterType().equals(Result.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 如果返回的是null，则返回成功但无数据的结果
        if (body == null) {
            return Result.success(null);
        }
        // 如果返回的已经是Result类型，则直接返回
        if (body instanceof Result) {
            return body;
        }
        // 否则包装成Result返回
        return Result.success(body);
    }
}
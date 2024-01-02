package com.example.demo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

public class ApiUtils {

    public static <T> ApiResult<T> success(T response){
        return new ApiResult<T>(true, response, null);
    }
    public static <T> ApiResult<T> error(String message, HttpStatus httpStatus){
        return new ApiResult<T>(false, null, new ApiError(message, httpStatus.value()));
    }

    // ** JSON으로 반환해야 할 데이터.
    @AllArgsConstructor @Getter
    public static class ApiResult<T>{
        private final boolean success; // 현재 상태
        private final T response; // 반환할 실제 데이터
        private final ApiError error;

        public String toString(){
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("success", success)
                    .append("response", response)
                    .append("error", error)
                    .toString();
        }
    }

    @AllArgsConstructor @Getter
    public static class ApiError{
        private final String message;
        private final int status;

        public String toString(){
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("message", message)
                    .toString();
        }
    }
}

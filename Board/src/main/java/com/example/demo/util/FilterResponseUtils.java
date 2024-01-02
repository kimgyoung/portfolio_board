package com.example.demo.util;

import com.example.demo.error.exception.Exception401;
import com.example.demo.error.exception.Exception403;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// ** 에러 메시지를 HTTP 응답 응답으로 설정하고 JSON 형태로 반환하는 유틸리티
public class FilterResponseUtils {

    // ** 401 에러
    public static void unAuthorized(HttpServletResponse response, Exception401 e) throws IOException {

        // ** HTTP 응답의 상태 코드를 설정. Exception401에서 상태 코드를 가져온다.
        response.setStatus(e.status().value());

        // ** 응답의 컨텐트 타입을 application/json; charset=utf-8으로 설정
        response.setContentType("application/json; charset=utf-8");

        // ** ObjectMapper 객체를 생성. Java 객체를 JSON 문자열로 변환하는 데 사용.
        ObjectMapper objectMapper = new ObjectMapper();

        // ** ObjectMapper를 사용해 Exception401의 본문을 JSON 문자열로 변환.
        String responseBody = objectMapper.writeValueAsString(e.body());

        // ** 변환된 JSON 문자열을 응답 본문에 작성
        response.getWriter().println(responseBody);
    }

    // ** 403 에러
    public static void forbidden(HttpServletResponse response, Exception403 e) throws IOException {

        // ** HTTP 응답의 상태 코드를 설정. Exception403에서 상태 코드를 가져온다.
        response.setStatus(e.status().value());

        // ** 응답의 컨텐트 타입을 application/json; charset=utf-8으로 설정
        response.setContentType("application/json; charset=utf-8");

        // ** ObjectMapper 객체를 생성. Java 객체를 JSON 문자열로 변환하는 데 사용.
        ObjectMapper objectMapper = new ObjectMapper();

        // ** ObjectMapper를 사용해 Exception403의 본문을 JSON 문자열로 변환.
        String responseBody = objectMapper.writeValueAsString(e.body());

        // ** 변환된 JSON 문자열을 응답 본문에 작성
        response.getWriter().println(responseBody);
    }
}

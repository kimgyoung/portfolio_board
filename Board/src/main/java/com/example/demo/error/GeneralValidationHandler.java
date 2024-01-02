package com.example.demo.error;

import com.example.demo.error.exception.Exception400;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

// ** 이 클래스가 AOP의 Aspect 역할을 할 것임.
@Aspect

// ** Spring의 빈으로 등록되어야 함
@Component
public class GeneralValidationHandler {
    // **  AOP(Aspect-Oriented Programming)를 적용할 것
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    @Before("postMapping()") // ** Pointcut 이 적용된 메소드가 실행되기 전에 호출
    public void validationAdvice(JoinPoint jp) {


        Object[] args = jp.getArgs();


        for (Object arg : args) {

            // ** 인자가 Errors 타입인지 확인.
            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;

                // ** 에러가 있는지 확인.
                if (errors.hasErrors()) {
                    // ** 에러가 있다면 Exception400 예외를 던짐.
                    throw new Exception400(
                            errors.getFieldErrors().get(0).getDefaultMessage()+":"+errors.getFieldErrors().get(0).getField()
                    );
                }
            }
        }
    }
}

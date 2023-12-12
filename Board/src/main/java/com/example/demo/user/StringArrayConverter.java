package com.example.demo.user;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringArrayConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        // ** 전달받은 리스트에 있는 구성요소들을 "," 로 구분하여 하나의 문자열로 변환 함.
        return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        // ** JPA 가 save를 할떄, select 부터 진행 하기 때문에 값이 null 인지 확인 해야 한다.
        if(dbData == null)
            return Collections.emptyList();
        else
            // ** 전달 받은 하나의 문자열을 "," 로 구분하여 리스트로 변환 함.
            return Arrays.stream(dbData.split(SPLIT_CHAR))
                    .map(String::valueOf)
                    .collect(Collectors.toList());
    }
}

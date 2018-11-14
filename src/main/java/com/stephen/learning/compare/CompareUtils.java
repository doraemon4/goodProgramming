package com.stephen.learning.compare;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

import java.util.*;

/**
 * @Auther: jack
 * @Date: 2018/11/9 10:50
 * @Description:
 */
public class CompareUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Data
    @Builder
    public static class Result{
        private String proCode;
        private String message;
    }

    public static Result getModifyContent(Object source, Object target) {
        StringBuffer stringBuffer=new StringBuffer();
        if (null == source || null == target) {
            if (null == source && null == target) return null;
            else if (null == target) return null;
            else {
                return mapper.convertValue(target, new TypeReference<Object>() {
                });
            }
        }
        if (!Objects.equals(source.getClass().getName(), target.getClass().getName())) {
            throw new ClassCastException("source and target are not same class type");
        }
        Map<String, Object> sourceMap = mapper.convertValue(source, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> targetMap = mapper.convertValue(target, new TypeReference<Map<String, Object>>() {});
        sourceMap.forEach((k, v) -> {
            Object targetValue = targetMap.get(k);
            if (!Objects.equals(v, targetValue)) {
                stringBuffer.append(k+"由原来的:"+v+",更新为:"+targetValue+";");
            }
        });
        return Result.builder().message(stringBuffer.toString()).build();
    }
}

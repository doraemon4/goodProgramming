package com.stephen.learning.parse;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther jack
 * @Date: 2018/9/27 21:41
 * @Description: 字符串映射为对象(使用jackson)
 */
@Slf4j
public class String2ObjectUtil{
    private final static ObjectMapper mapper = new ObjectMapper();

    public static List parse(String jsonStr, Class T){
        try{
            JavaType javaType = getCollectionType(ArrayList.class,T);
            List configList =  mapper.readValue(jsonStr, javaType);
            return configList;
        }catch (Exception e){
            log.error("解析文件错误");
        }
        return null;
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}

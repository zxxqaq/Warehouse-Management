package com.sirius.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {
    }

    public static <T> T beanCopy(Object source, Class<T> c) {
        T result = null;

        try {
            result = c.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <T> List<T> beanCopyList(List<?> list, Class<T> c) {
        return list.stream().map(o -> beanCopy(o, c)).collect(Collectors.toList());
    }
}
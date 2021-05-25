package com.kuang.utils;

import org.junit.Test;

import java.util.UUID;

/**
 * @author wbw
 * @date 2021/5/17 15:38
 */
public class IDutils {
    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Test
    public void test(){
        String id = IDutils.getId();
        System.out.println(id);
    }
}

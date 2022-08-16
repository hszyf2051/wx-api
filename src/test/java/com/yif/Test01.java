package com.yif;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yif
 */
public class Test01 {

    @Test
    public void SetMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("123","ffff");
        map.put("77","7777");
        System.out.println(map.get("123"));
        System.out.println(map.get("222"));
    }

    public static void main(String[] args) {
    }
}

package com.yif.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author Yif
 */
@Slf4j
@SpringBootTest
public class DoctorControllerTest {

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext wac;

    @Test
    void SelectOne() {
//        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        // 发送get请求：
////        RequestBuilder request = get("http://ai.foemy.com/doctor/fingDoctor?id=5063");
//        try {
//            String response = mvc.perform(request).andReturn().getResponse().getContentAsString();
//            System.out.println(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
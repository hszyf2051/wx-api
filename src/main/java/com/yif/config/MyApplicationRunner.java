package com.yif.config;

import com.yif.service.IDoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Slf4j
@Component
@Order(value = 1)
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    private IDoctorService doctorService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        log.info("测试MyApplicationRunner");
//        doctorService.readDoctors();
    }
}

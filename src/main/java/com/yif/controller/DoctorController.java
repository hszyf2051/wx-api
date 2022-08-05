package com.yif.controller;

/**
 * @author admin
 */

import com.yif.entity.Doctor;
import com.yif.service.IDoctorService;
import com.yif.util.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Api(tags = "Api-Doctor")
public class DoctorController {
    @Value("${doctor.corpid}")
    private String corpid;

    @Value("${doctor.corpsecret}")
    private String corpsecret;

    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/getWxToken")
    @ApiOperation(value = "获取企业微信Token")
    public String getToken(@RequestParam String corpid, @RequestParam String corpsecret) {
        String token = doctorService.getToken(corpid, corpsecret);
        // 设置缓存时间为1h
        redisUtils.set("token",token,1,TimeUnit.HOURS);
        return token;
    }

    /**
     * 查找所有医生
     * @param model
     * @return
     */
    @GetMapping("/getDoctors")
    public ModelAndView getDoctors(Model model) {
        List<Doctor> doctors = doctorService.readDoctors();
        model.addAttribute("doctors",doctors);
        return new ModelAndView("index");
    }

    /**
     * 根据id查找对应的医生
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/findDoctorById")
    public ModelAndView findDoctorById(Model model,@RequestParam String id) {
        List<Doctor> doctors = doctorService.findDoctorById(id);
        model.addAttribute("doctors",doctors);
        return new ModelAndView ("index");
    }

    /**
     * 根据id给指定医生发送消息
     * @param id
     * @return
     */
    @GetMapping("/sendDoctorMsgById")
    @ApiOperation(value = "根据id给指定医生发送消息")
    public String sendMsg(@RequestParam String id) {
        String token = redisUtils.get("token");
        String msg;
        if (token == null) {
            // 获取新的token
            this.getToken(corpid, corpsecret);
            String tokenNew = redisUtils.get("token");
            log.info("token不存在，已重新生成");
            msg = doctorService.sendMsg(tokenNew,id);
        } else {
            log.info("token存在，发送消息");
            msg = doctorService.sendMsg(token,id);
        }
        return msg;
    }

    /**
     * 给每个医生发送自己的信息统计
     */
    @GetMapping("/sendDoctorMsgAll")
    @ApiOperation(value = "给每个医生发送自己的信息统计")
    public void sendMsgAll() {
        // 获取Excel所有的成员信息
        List<Doctor> doctors = doctorService.readDoctors();
        for (Doctor doctor : doctors) {
            String id = doctor.getId();
            this.sendMsg(id);
        }
    }
}

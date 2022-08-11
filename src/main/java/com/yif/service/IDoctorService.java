package com.yif.service;

import com.yif.entity.Doctor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author yif
 */
public interface IDoctorService {
    /**
     * 获取token
     * @param corpid
     * @param corpsecret
     * @return
     */
    public String getToken(String corpid,String corpsecret);

    /**
     * 获取医生信息
     * @return
     */
    public List<Doctor> readDoctors();

    public Map<String, Object> readDoctors2();

    public List<Doctor> findAllDoctor();

    /**
     * 给客户发送消息
     * @param token
     * @param id
     * @return
     */
    public String sendMsg(String token,String id) throws IOException;

    /**
     * 获取用户授权
     */
    String accessUser();

    /**
     * 给所有人发送消息
     */
    void sendAllMsg()  throws UnsupportedEncodingException;

    Doctor findDoctor(String id) throws ParseException;
}

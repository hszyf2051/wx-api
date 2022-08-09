package com.yif.service;

import com.yif.entity.Doctor;

import java.util.List;

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

    public List<Doctor> readDoctors();

    public List<Doctor> findDoctorById(String id);

    /**
     * 给客户发送消息
     * @param token
     * @param id
     * @return
     */
    public String sendMsg(String token,String id);

    /**
     * 获取用户授权
     */
    String accessUser();
}

package com.yif.service;

import com.yif.entity.User;

import java.util.List;

/**
 * @author admin
 */
public interface IUserService {

    /**
     * 获取token
     * @param corpid
     * @param corpsecret
     * @return
     */
    public String getToken(String corpid,String corpsecret);

    /**
     * 获取部门成员信息，并写入Excel
     * @param token
     * @param id
     * @return
     */
    public List<User> getUsers(String token,String id);

    /**
     * 从Excel中读取信息
     * @return
     */
    public List<User> readUsers();

    /**
     * 根据用户名
     * @param id
     * @return
     */
    public List<User> findUserById(String id);

    public String sendMsg(String token,String id);
}

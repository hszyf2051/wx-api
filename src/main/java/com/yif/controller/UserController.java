package com.yif.controller;

import com.yif.entity.User;
import com.yif.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author admin
 */


@RestController
@Slf4j
@Api(tags = "Api-User")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/getToken")
    @ApiOperation(value = "获取企业微信Token")
    public String getToken(@RequestParam String corpid, @RequestParam String corpsecret) {
        String token = userService.getToken(corpid, corpsecret);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("token",token);
        return token;
    }

    @GetMapping("/getUser")
    @ApiOperation(value = "获取部门成员信息", notes = "Excel获取成员信息")
    public List getUsers(@RequestParam String token, @RequestParam(required = false) String id) {
        List<User> users = userService.getUsers(token, id);
        return users;
    }

    @GetMapping("/readUsers")
    public ModelAndView  readUsers(Model model) {
        List<User> userList = userService.readUsers();
        model.addAttribute("users",userList);
        return new ModelAndView("User");
    }

    /**
     * 根据id查找对应的成员
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/findUserById")
    public ModelAndView findDoctorById(Model model,@RequestParam String id) {
        List<User> userList = userService.findUserById(id);
        model.addAttribute("users",userList);
        return new ModelAndView("User");
    }

    @GetMapping("/sendMsgById")
    public String sendMsg(@RequestParam String id) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String token = (String) valueOperations.get("token");
        log.info(id);
        String msg = userService.sendMsg(token,id);
        return msg;
    }

    @GetMapping("/getTk")
    public String getToken() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String token = (String) valueOperations.get("token");
        return token;
    }

}

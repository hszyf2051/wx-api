package com.yif.controller;

/**
 * @author admin
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yif.entity.Doctor;
import com.yif.service.IDoctorService;
import com.yif.util.HttpUtil;
import com.yif.util.OuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@Api(tags = "Api-Doctor")
public class DoctorController {
    @Value("${doctor.corpid}")
    private String corpid;

    @Value("${doctor.corpsecret}")
    private String corpsecret;

    @Value("${doctor.redirect_uri}")
    private String url;

    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private OuthUtil outhUtil;


    @GetMapping("/getWxToken")
    @ApiOperation(value = "获取企业微信Token")
    public String getToken() {
        String token = doctorService.getToken(corpid, corpsecret);
        return token;
    }

    @ApiOperation(value = "微信授权回调接口")
    @RequestMapping("/callBack")
    public ModelAndView callBack(HttpServletRequest request, HttpServletResponse response,Model model)throws Exception {
        String code = request.getParameter("code");
        String sessionUserId = (String)request.getSession().getAttribute("userId");
        String outh2Url = outhUtil.getOuth2Url();
        if(StringUtils.isEmpty(sessionUserId)){
            // code 不为0，用户成功授权登录
            if (StringUtils.isNotEmpty(code)) {
                // 获取获取access_token
                String token = this.getToken();
                // 拼接url
                String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + token + "&code=" + code;
                // 获取用户身份
                String httpPost = HttpUtil.httpPost(url, (Map<String, Object>) null);
                // 获取微信用户的UserId
                JSONObject jsonObject = JSON.parseObject(httpPost);
                String userId = jsonObject.getString("UserId");
                // 校验医生是否存在
                Doctor doctor = doctorService.findDoctor((String) userId);
                if(doctor!=null){
                    // 获取医生信息
                    request.getSession().setAttribute("userId", userId);
                    model.addAttribute("doctors", doctor);
                    return new ModelAndView("index");
                }else{
                    // 医生不存在
                    log.info("该医生不存在！"+ outh2Url);
                    response.sendRedirect(outh2Url);

                }
            } else {
                // code 失效
                log.info("code不存在"+ outh2Url);

                response.sendRedirect(outh2Url);

            }
        }else{
            // 获取医生信息
            Doctor doctor = doctorService.findDoctor((String) sessionUserId);
            model.addAttribute("doctors", doctor);
            return new ModelAndView("index");
        }
        return null;
    }

    /**
     * 查找所有医生
     * @param model
     * @return
     */
    @GetMapping("/getDoctors")
    public ModelAndView getDoctors(Model model) {
        Map<String, Object> doctors = doctorService.readDoctors();
        model.addAttribute("doctors",doctors);
        return new ModelAndView("index");
    }

    /**
     * 找到所有医生信息
     * @param model
     * @return
     */
    @GetMapping("/findAllDoctor")
    @ApiOperation(value = "查找所有医生信息")
    public List<Doctor> findDoctorById(Model model) {
        List<Doctor> doctors = doctorService.findAllDoctor();
        return doctors;
    }

    /**
     * 根据id查找单个医生并返回给页面数据
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/findDoctor")
    @ApiOperation(value = "根据id查找医生")
    public ModelAndView findDoctor(Model model,@RequestParam String id) throws ParseException {
        Doctor doctor = doctorService.findDoctor(id);
        log.info("根据id查询到的医生："+doctor);
        model.addAttribute("doctors",doctor);
        return new ModelAndView ("content");
    }

    @GetMapping("/redirect")
    public ModelAndView redirect(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
        String sessionUserId = (String)request.getSession().getAttribute("userId");
        String outh2Url = outhUtil.getOuth2Url();
        if(StringUtils.isEmpty(sessionUserId)){
            // code 失效
            response.sendRedirect(outh2Url);
        }else{
            // 获取医生信息
            Doctor doctor = doctorService.findDoctor((String) sessionUserId);
            model.addAttribute("doctors", doctor);
            return new ModelAndView("content");
        }
        return null;
    }

    /**
     * 给每个医生发送自己的信息统计
     */
    @GetMapping("/sendDoctorMsgAll")
    @ApiOperation(value = "给每个医生发送自己的信息统计")
    public void sendMsgAll() throws IOException{
        doctorService.sendAllMsg();
    }

    /**
     * 回调数据，重新刷新excel数据
     * @return
     */
    @GetMapping("/reloadExcelData")
    @ApiOperation(value = "回调数据")
    public Map<String, Object> reloadExcelData() {
        return doctorService.reload();
    }

    @GetMapping("/reloadSend")
    @ApiOperation(value = "给修改后的医生发送自己的信息统计")
    public void reloadSend() throws IOException{
        doctorService.reloadSend();
    }

    @GetMapping("/reloadGet")
    @ApiOperation(value = "获取修改后的医生信息统计")
    public Map<String, Object> reloadGet() throws IOException{
        return doctorService.readDoctors();
    }
}

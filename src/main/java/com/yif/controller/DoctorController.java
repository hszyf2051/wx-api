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
import com.yif.util.RedisUtils;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private RedisUtils redisUtils;

    @Autowired
    private OuthUtil outhUtil;


    @GetMapping("/getWxToken")
    @ApiOperation(value = "获取企业微信Token")
    public String getToken() {
        String token = doctorService.getToken(corpid, corpsecret);
        System.out.println(token);
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
                System.out.println("UserId:"+ userId);
                // 校验医生是否存在
                if(doctorService.findDoctor(userId)!=null){
                    // 获取医生信息
                    Doctor doctor = doctorService.findDoctor((String) userId);
                    request.getSession().setAttribute("userId", userId);
                    model.addAttribute("doctors", doctor);
                    return new ModelAndView("index");
                }else{
                    // 医生不存在
                    response.sendRedirect(outh2Url);
                    System.out.println("该医生不存在！");
                }
            } else {
                // code 失效
                response.sendRedirect(outh2Url);
                System.out.println("code失效");
            }
        }else{
            // 获取医生信息
            Doctor doctor = doctorService.findDoctor((String) sessionUserId);
            model.addAttribute("doctors", doctor);
            return new ModelAndView("index");
        }
        return null;
    }

    @GetMapping("/getDoctor")
    @ApiOperation(value = "点击图片展现医生信息")
    public String getDoctor(@RequestParam String code) {


        // 如果有  发返回医生信息

        // 如果没有

        //  if（code 有）取  从微信接口获取userid  https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE
        String token = this.getToken();
        String httpPost = HttpUtil.httpPost("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE" + token+"&code=" + code, (Map<String, Object>) null);
        //获取到userId
//        没有code 或者 获取userId 失败
//                redirec_url ---从定向到网页授权链接

       //  取到userid
                 //map 中获取 发返回医生信息

       // userid  存放到session中去





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
        //request.getSession().getAttribute("userId");

        Doctor doctor = doctorService.findDoctor(id);
        model.addAttribute("doctors",doctor);
        return new ModelAndView ("index");
    }

//    @GetMapping("/redirect")
//    public ModelAndView redirect(Model model,@RequestParam String id) throws ParseException {
//        Doctor doctor = doctorService.findDoctor(id);
//        model.addAttribute("doctors",doctor);
//        return new ModelAndView ("content");
//    }

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


    private String getOuth2Url() throws UnsupportedEncodingException {
        String redirect_uri = URLEncoder.encode(url, "UTF-8");
        String wxUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=APPID" +
                "&redirect_uri=REDIRECT_URI"+
                "&response_type=code" +
                "&scope=SCOPE" +
                "&state=123#wechat_redirect";
       return wxUrl.replace("APPID",corpid).replace("REDIRECT_URI",redirect_uri).replace("SCOPE","snsapi_userinfo");
    }
}

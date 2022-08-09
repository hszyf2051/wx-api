package com.yif.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yif.entity.Doctor;
import com.yif.service.IDoctorService;
import com.yif.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Slf4j
@Service
public class DoctorServiceImpl implements IDoctorService {
    @Value("${doctor.jpgurl}")
    private String jpgurl;

    @Value("${doctor.redirect_uri}")
    private String url;

    @Value("${doctor.corpid}")
    private String appid;

    @Value("${doctor.scope}")
    private String scope;

    @Value("${doctor.agentid}")
    private String agentid;

    /**
     * 获取企业token
     * @param corpid
     * @param corpsecret
     * @return
     */
    @Override
    public String getToken(String corpid, String corpsecret) {
        Map map = new HashMap();
        map.put("corpid",corpid);
        map.put("corpsecret",corpsecret);
        String s = HttpUtil.httpGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken", null, map);
        JSONObject jsonObject = JSON.parseObject(s);
        String access_token = jsonObject.getString("access_token");
        return access_token;
    }

    /**
     * 从yml中读取文件路径
     */
    @Value("${doctor.fileName}")
    private String fileName;

    @Override
    public List<Doctor> readDoctors() {
        // 创建一个数据格式来承装读取到数据
        Class<Doctor> head = Doctor.class;
        // 创建ExcelReader对象
        List<Doctor> doctorList = new ArrayList<>();
        ExcelReader excelReader = EasyExcel.read(fileName, head, new AnalysisEventListener<Doctor>() {
            @Override
            public void invoke(Doctor doctor, AnalysisContext analysisContext) {
                if (doctor.getHealing() == null) {
                    // 住院患者治疗量
                    doctor.setHealing(0);
                }
                if (doctor.getBirthOperations() == null) {
                    // 生日当天手术量
                    doctor.setBirthOperations(0);
                }
                if (doctor.getBirthVisits() == null) {
                    // 生日当天看诊量
                    doctor.setBirthVisits(0);   
                }
                doctorList.add(doctor);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            }
        }).build();
        // 创建sheet对象,并读取Excel的第1个sheet(下标从0开始)
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 关闭流操作，在读取文件时会创建临时文件,如果不关闭,磁盘爆掉
        excelReader.finish();
        log.info(String.valueOf(doctorList));
        return doctorList;

    }

    /**
     * 根据id查找指定人信息
     * @param id
     * @return
     */
    @Override
    public List<Doctor> findDoctorById(String id) {
        // 创建一个数据格式来承装读取到数据
        Class<Doctor> head = Doctor.class;
        // 创建ExcelReader对象
        List<Doctor> doctorList = new ArrayList<>();
        ExcelReader excelReader = EasyExcel.read(fileName, head, new AnalysisEventListener<Doctor>() {
            @Override
            public void invoke(Doctor doctor, AnalysisContext analysisContext) {
                if (doctor.getId().equals(id)) {
                    if (doctor.getHealing() == null) {
                        // 住院患者治疗量
                        doctor.setHealing(0);
                    }
                    if (doctor.getBirthOperations() == null) {
                        // 生日当天手术量
                        doctor.setBirthOperations(0);
                    }
                    if (doctor.getBirthVisits() == null) {
                        // 生日当天看诊量
                        doctor.setBirthVisits(0);
                    }
                    doctorList.add(doctor);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                if (doctorList.size() == 0) {
                    log.info("该企业账号:"+ id +",查无此人");
                }
            }
        }).build();
        // 创建sheet对象,并读取Excel的第1个sheet(下标从0开始)
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 关闭流操作，在读取文件时会创建临时文件,如果不关闭,磁盘爆掉
        excelReader.finish();
        log.info(String.valueOf(doctorList));
        return doctorList;
    }

    /**
     * 发送图文消息
     * @param token
     * @param id
     * @return
     */
    @Override
    public String sendMsg(String token,String id) {
        // 参数
        Map<String, Object> paramMap = new HashMap<>();
        // 图文内容
        Map newsMap = new HashMap();
        // 发送图文的api中articles的类型是数组
        ArrayList<Object> arrayList = new ArrayList<>();
        Map articlesMap = new HashMap();
        articlesMap.put("title","医生节活动");
        articlesMap.put("description","救死扶伤，大爱无疆");
        articlesMap.put("url","http://192.168.3.216:8181/findDoctorById?id="+id);
        articlesMap.put("picurl",jpgurl);
        arrayList.add(articlesMap);
        paramMap.put("touser",id);
        paramMap.put("msgtype", "news");
//        paramMap.put("toparty", 3);
        paramMap.put("agentid", 2);
        paramMap.put("news", newsMap);
        newsMap.put("articles", arrayList);

        String jsonObject = String.valueOf(new JSONObject(paramMap));
        String httpPost = HttpUtil.httpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token, (Map<String, Object>) null, jsonObject);
        return httpPost;
    }

    @Override
    public String accessUser() {
        return null;
    }

//    @Override
//    public String accessUser(HttpServletResponse response)throws IOException {
//        String urlencoder = URLEncoder.encode(url,"UTF-8");
//        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
//                "appid=APPID" +
//                "&redirect_uri=REDIRECT_URI"+
//                "&response_type=code" +
//                "&scope=SCOPE" +
//                "&state=123#wechat_redirect";
//
//        return httpPost;
//    }
}

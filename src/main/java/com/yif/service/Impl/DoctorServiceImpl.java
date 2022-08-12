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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author admin
 */
@Slf4j
@Service
public class DoctorServiceImpl implements IDoctorService {

    @Value("${doctor.corpsecret}")
    private String corpsecret;

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


    private  Map<String, Object> doctorData;

    /**
     * 读取excel数据
     */
    @PostConstruct
    void initDocetorData(){
        doctorData = this.readDoctors2();
        System.out.println(doctorData);
    }



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

    @Override
    public Map<String, Object> readDoctors2() {
        // 创建一个数据格式来承装读取到数据
        Class<Doctor> head = Doctor.class;
        // 创建ExcelReader对象
        Map<String, Object> doctorMap = new HashMap<>();
        ExcelReader excelReader = EasyExcel.read(fileName, head, new AnalysisEventListener<Doctor>() {
            @Override
            public void invoke(Doctor doctor, AnalysisContext analysisContext) {
                doctorMap.put(doctor.getId(),doctor);
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
        log.info(String.valueOf(doctorMap));
        return doctorMap;
    }

    /**
     * 根据id查找指定人信息
     * @return
     */
    @Override
    public List<Doctor> findDoctorById() {
        List<Doctor> doctors = new ArrayList<>();
        List<Doctor> doctors1 = this.readDoctors();
        for (Map.Entry<String, Object> doctor : doctorData.entrySet()) {
            String jsonString = JSON.toJSONString(doctor);
            Doctor doctor1 = JSON.parseObject(jsonString, Doctor.class);
        }
        return doctors1;
    }

    public String sendMesToAll(){
        //获取token

        //遍历Map  取出发送人

//        try{
//            //单个消息  判断成功失败
//            this.sendMsg();
//            //成功  记录成功人KEY  记录一下成功文件   内存属性发送标记
//
//            //失败  记录失败人KEY  记录一下失败文件
//
//        }catch (Exception e){
//            log.info("eror。。。。");
//        }
        return null;
    }

    /**
     * 发送图文消息
     * @param token
     * @param id
     * @return
     */
    @Override
    public String sendMsg(String token,String id) throws UnsupportedEncodingException {
//        String redirect_uri = URLEncoder.encode(url, "UTF-8");
//        String wxUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
//                "appid=APPID" +
//                "&redirect_uri=REDIRECT_URI"+
//                "&response_type=code" +
//                "&scope=SCOPE" +
//                "&state=123#wechat_redirect";
//        wxUrl = wxUrl.replace("APPID",appid).replace("REDIRECT_URI",redirect_uri).replace("SCOPE","snsapi_userinfo");
        // 参数
        Map<String, Object> paramMap = new HashMap<>();
        // 图文内容
        Map newsMap = new HashMap();
        // 发送图文的api中articles的类型是数组
        ArrayList<Object> arrayList = new ArrayList<>();
        Map articlesMap = new HashMap();
        articlesMap.put("title","医生节活动");
        articlesMap.put("description","医者仁心，大爱无疆。亲爱的医生，向您致以最崇高的敬意与感谢，您辛苦了！祝您节日快乐！");
//        articlesMap.put("url","http://192.168.3.216:8181/findDoctorById?id="+id);
        articlesMap.put("url","http://foemy.asuscomm.com:8181/findDoctor?id="+id);
//        articlesMap.put("url",wxUrl);
        articlesMap.put("picurl",jpgurl);
        arrayList.add(articlesMap);
        paramMap.put("touser",id);
        paramMap.put("msgtype", "news");
        paramMap.put("agentid", 2);
        paramMap.put("news", newsMap);
        newsMap.put("articles", arrayList);

        String jsonObject = String.valueOf(new JSONObject(paramMap));
        String httpPost = HttpUtil.httpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token, (Map<String, Object>) null, jsonObject);
        return httpPost;
    }

    /**
     * 转化格式 yyyy-MM-dd
     * @param date
     * @return
     * @throws ParseException
     */
    public String formatDate(String date) throws ParseException {
        String replaceDate = date.replaceAll("/", "-");
        Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(replaceDate);
        String now = new SimpleDateFormat("yyyy年M月d日").format(newDate);
        return now;
    }

    /**
     * 转化格式 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     * @throws ParseException
     */
    public String formatDate2(String date) throws ParseException {
        String replaceDate = date.replaceAll("/", "-");
        Date newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replaceDate);
        String now = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss").format(newDate);
        return now;
    }

    /**
     * 转化格式 MM-dd
     * @param date
     * @return
     * @throws ParseException
     */
    public String formatDate3(String date) throws ParseException {
        String replaceDate = date.replaceAll("/", "-");
        Date newDate = new SimpleDateFormat("MM-dd").parse(replaceDate);
        String now = new SimpleDateFormat("M月d日 ").format(newDate);
        return now;
    }


    @Override
    public String accessUser() {
        return null;
    }

    @Override
    public void sendAllMsg()  throws UnsupportedEncodingException{
        //获取token
        String token = this.getToken(appid, corpsecret);
        // 遍历拿到所有医生的id 取出发送人
        for (String doctorId : doctorData.keySet()) {
            try {
                //单个消息  判断成功失败
                String msg = this.sendMsg(token, doctorId);
                log.info(msg+"!!!!!!");
                //成功  记录成功人KEY  记录一下成功文件   内存属性发送标记
                //失败  记录失败人KEY  记录一下失败文件
            } catch (Exception e) {
                log.info("error 失败了");
            }
        }




//        try{
//            //单个消息  判断成功失败
//            this.sendMsg();
//            //成功  记录成功人KEY  记录一下成功文件   内存属性发送标记
//
//            //失败  记录失败人KEY  记录一下失败文件
//
//        }catch (Exception e){
//            log.info("eror。。。。");
//        }
    }

    @Override
    public Doctor findDoctor(String id) {
        // 创建一个数据格式来承装读取到数据
        Class<Doctor> head = Doctor.class;
        // 创建ExcelReader对象
        Doctor doctorNew = new Doctor();
//        List<Doctor> doctorList = new ArrayList<>();
        ExcelReader excelReader = EasyExcel.read(fileName, head, new AnalysisEventListener<Doctor>() {
            @Override
            public void invoke(Doctor doctor, AnalysisContext analysisContext) {
                if (doctor.getId().equals(id)) {
                    String MaxTimeOperations = doctor.getMaxTimeOperations().replace("分钟", "");
                    doctor.setMaxTimeOperations(MaxTimeOperations);
                    // 去除excel中的分钟
                    BeanUtils.copyProperties(doctor,doctorNew);
                }
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                if (doctorNew == null) {
                    log.info("该企业账号:" + id + ",查无此人");
                }
            }
        }).build();
        // 创建sheet对象,并读取Excel的第1个sheet(下标从0开始)
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 关闭流操作，在读取文件时会创建临时文件,如果不关闭,磁盘爆掉
        excelReader.finish();
        log.info(String.valueOf(doctorNew));
        return doctorNew;
    }
}

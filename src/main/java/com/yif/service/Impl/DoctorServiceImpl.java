package com.yif.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yif.entity.Doctor;
import com.yif.param.RightMsg;
import com.yif.param.WrongMsg;
import com.yif.service.IDoctorService;
import com.yif.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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

    @Value("${doctor.agentid}")
    private String agentid;

    /**
     * 从yml中读取文件路径
     */
    @Value("${doctor.fileName}")
    private String fileName;

    /**
     * 需要重新发送的医生数据
     */
    @Value("${doctor.wrongFile}")
    private String wrongFile;

//    /**
//     * 动态监测修改的文件夹目录
//     */
//    @Value("${doctor.file}")
//    private String file;

    /**
     * 发送消息成功url
     */
    @Value("${doctor.rightMsgUrl}")
    private String rightMsgUrl;

    /**
     * 发送消息失败url
     */
    @Value("${doctor.wrongMsgUrl}")
    private String wrongMsgUrl;

    /**
     * reload发送消息失败url
     */
    @Value("${doctor.reloadWrongUrl}")
    private String reloadWrongUrl;

    /**
     * 发送成功医生信息Excel
     */
    @Value("${doctor.sendSuccessExcel}")
    private String sendSuccessExcel;

    /**
     * 缓存数据
     */
    private Map<String, Object> doctorData;

    /**
     * 重载数据
     */
    private Map<String, Object> reloadData;

    /**
     * 开启项目读取excel数据
     */
    @PostConstruct
    public void initDocetorData(){
        doctorData = this.readDoctors2();
        Set<Map.Entry<String, Object>> entries = doctorData.entrySet();
//        log.info(String.valueOf(doctorData));
    }

    /**
     * 获取reload后的数据
     */
    @PostConstruct
    public void initReloadData(){
        reloadData = this.readDoctors();
//        log.info(String.valueOf(reloadData));
    }

//    /**
//     * 监控Excel文件变化
//     * @throws Exception
//     */
//    @PostConstruct
//    public void Monitor() throws Exception{
//        try {
//            this.FileRunner();
//        } catch (Exception e) {
//            log.info("监控异常");
//            throw new RuntimeException(e);
//        }
//    }



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
     * 重新获取的医生数据
     * @return
     */
    @Override
    public Map<String, Object> readDoctors() {
        // 创建一个数据格式来承装读取到数据
        Class<Doctor> head = Doctor.class;
        // 创建ExcelReader对象
        Map<String, Object> doctorMap = new HashMap<>();
        ExcelReader excelReader = EasyExcel.read(wrongFile, head, new AnalysisEventListener<Doctor>() {
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
        // 将reloadData放入缓存中
        reloadData = doctorMap;
        return doctorMap;
    }

    /**
     * 从Excel读取数据
     * @return
     */
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
        return doctorMap;
    }

    /**
     * 查询所有医生信息
     * @return
     */
    @Override
    public List<Doctor> findAllDoctor() {
        List<Doctor> doctors = new ArrayList<>();
        for (Map.Entry<String, Object> doctor : doctorData.entrySet()) {
            String jsonString = JSON.toJSONString(doctor.getValue());
            Doctor doctor1 = JSON.parseObject(jsonString, Doctor.class);
            doctors.add(doctor1);
        }
        return doctors;
    }

    /**
     * 发送图文消息
     * @param token
     * @param id
     * @return
     */
    @Override
    public String sendMsg(String token,String id) throws UnsupportedEncodingException {
        // 参数
        Map<String, Object> paramMap = new HashMap<>();
        // 图文内容
        Map newsMap = new HashMap();
        // 发送图文的api中articles的类型是数组
        ArrayList<Object> arrayList = new ArrayList<>();
        Map articlesMap = new HashMap();
        articlesMap.put("title","医师节祝福");
        articlesMap.put("description","医者仁心，大爱无疆。亲爱的医生，向您致以最崇高的敬意与感谢。您辛苦了！祝您节日快乐！");
        articlesMap.put("url",url);
        articlesMap.put("picurl",jpgurl);
        arrayList.add(articlesMap);
        paramMap.put("touser",id);
        paramMap.put("msgtype", "news");
        paramMap.put("agentid", agentid);
        paramMap.put("news", newsMap);
        newsMap.put("articles", arrayList);
        String jsonObject = String.valueOf(new JSONObject(paramMap));
        String httpPost = HttpUtil.httpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token, (Map<String, Object>) null, jsonObject);
        log.info("微信返回:{}",httpPost);
        return httpPost;
    }
    /**
     * 根据id查找医生
     * @param id
     * @return
     */
    @Override
    public Doctor findDoctor(String id) throws ParseException {
        Doctor doctorNew = new Doctor();
        List<Doctor> allDoctor = this.findAllDoctor();
        for (Doctor doctor : allDoctor) {
            if (StringUtils.equals(doctor.getId(),id)) {
                if(!StringUtils.isEmpty(doctor.getMaxTimeOperations())){
                    doctor.setMaxTimeOperations(doctor.getMaxTimeOperations().replace("分钟",""));
                }
                // 格式手术时长最长日期
                if (!StringUtils.isEmpty(doctor.getMaxDate()))
                {
                    try {
                        if (doctor.getMaxDate().contains("/")) {
                            String maxDate = doctor.getMaxDate();
                            doctor.setMaxDate(this.formatDate(maxDate));
                        } else {
                            doctor.setMaxDate(doctor.getMaxDate());
                        }
                    } catch (Exception e) {
                        doctor.setMaxDate(doctor.getMaxDate());
                    }
                }
                // 格式手术最长结束时间
                try {
                    if (!StringUtils.isEmpty(doctor.getLatestTimeOperations())){
                        if (doctor.getLatestTimeOperations().contains("/")) {
                            String latestTimeOperations = doctor.getLatestTimeOperations();
                            doctor.setLatestTimeOperations(this.formatDate2(latestTimeOperations));
                        } else {
                            doctor.setLatestTimeOperations(doctor.getLatestTimeOperations());
                        }
                    }
                } catch (Exception e) {
                    doctor.setLatestTimeOperations(doctor.getLatestTimeOperations());
                }
                // 格式医生生日
                try {
                    if (doctor.getBirthDate()!=null) {
                        if (doctor.getBirthDate().contains("-")) {
                            String birthDate = doctor.getBirthDate();
                            doctor.setBirthDate(this.formatDate3(birthDate));
                        } else {
                            doctor.setBirthDate(doctor.getBirthDate());
                        }
                    }
                } catch (Exception e) {
                    doctor.setBirthDate(doctor.getBirthDate());
                }
                BeanUtils.copyProperties(doctor,doctorNew);
            }
        }
        return doctorNew;
    }

    /**
     * 给所有人发送消息
     * @throws UnsupportedEncodingException
     */
    @Override
    public void sendAllMsg()  throws UnsupportedEncodingException{
        //获取token
        String token = this.getToken(appid, corpsecret);
        // 遍历拿到所有医生的id 取出发送人
        List<RightMsg> rightMsg = new ArrayList<>();
        List<WrongMsg> wrongMsg = new ArrayList<>();
        for (String doctorId : doctorData.keySet()) {
            try {
                // 单个消息  判断成功失败
                String msg = this.sendMsg(token, doctorId);
                // 转化json对象
                JSONObject jsonMsg = JSONObject.parseObject(msg);
                String code = jsonMsg.getString("errcode");
                if (StringUtils.equals(code,"0")) {
                    // 成功  记录成功人KEY  记录一下成功文件   内存属性发送标记
                    RightMsg rightData = new RightMsg();
                    rightData.setId(doctorId);
                    rightMsg.add(rightData);
                } else {
                    // 失败  记录失败人KEY  记录一下失败文件
                    WrongMsg wrongData = new WrongMsg();
                    wrongData.setId(doctorId);
                    wrongMsg.add(wrongData);
                }
            } catch (Exception e) {
                log.error("服务器出错了 (ó﹏ò｡)", e);
            }
        }
        // 移除发送失败的doctorId
        for (WrongMsg msg : wrongMsg) {
            doctorData.remove(msg.getId());
        }
//        log.info(String.valueOf(doctorData));
//        log.info();

        // 将成功发送人的数据写入Excel
        String rightFilename = rightMsgUrl;
        ExcelWriter excelWriterRight = EasyExcel.write(rightFilename, RightMsg.class).build();
        WriteSheet writeSheetRight = EasyExcel.writerSheet("发送成功医生信息").build();
        excelWriterRight.write(rightMsg,writeSheetRight);
        excelWriterRight.finish();
        // 将失败发送的id写入Excel
        String wrongFilename = wrongMsgUrl;
        ExcelWriter excelWriterWrong = EasyExcel.write(wrongFilename, WrongMsg.class).build();
        WriteSheet writeSheetWrong = EasyExcel.writerSheet("发送失败医生id").build();
        excelWriterWrong.write(wrongMsg,writeSheetWrong);
        excelWriterWrong.finish();
        log.info(String.valueOf(rightMsg));
        log.info(String.valueOf(wrongMsg));
    }



    /**
     * 转化格式 yyyy-MM-dd
     * @param date
     * @return
     * @throws ParseException
     */
    public String formatDate(String date) {
        try {
            String replaceDate = date.replaceAll("/", "-");
            Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(replaceDate);
            String now = new SimpleDateFormat("yyyy年M月d日").format(newDate);
            return now;
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 转化格式 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     * @throws ParseException
     */
    public String formatDate2(String date)  {
        try {
            String replaceDate = date.replaceAll("/", "-");
            Date newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replaceDate);
            String now = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss").format(newDate);
            return now;
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 转化格式 MM-dd
     * @param date
     * @return
     * @throws ParseException
     */
    public String formatDate3(String date) {
        try {
            String replaceDate = date.replaceAll("/", "-");
            Date newDate = new SimpleDateFormat("MM-dd").parse(replaceDate);
            String now = new SimpleDateFormat("M月d日 ").format(newDate);
            return now;
        } catch (ParseException e) {
            return date;
        }
    }

//    public void FileRunner() throws Exception{
//        FileMonitor fileMonitor = new FileMonitor(1000);
//        fileMonitor.monitor(file, new FileListener());
//        fileMonitor.start();
//    }

    /**
     * 回调Excel数据
     */
    @Override
    public Map<String, Object> editorExcelData() {
        // 重新给Excel赋值
        Map<String, Object> stringObjectMap = readDoctors2();
        Set<Map.Entry<String, Object>> entries = stringObjectMap.entrySet();
        log.info("修改后："+String.valueOf(entries));
        return stringObjectMap;
    }

    @Override
    public Map<String, Object> reload() {
        // 重新刷新缓存
        doctorData = this.readDoctors2();
        Set<Map.Entry<String, Object>> entries = doctorData.entrySet();
//        log.info("修改后数据："+String.valueOf(doctorData));
        return doctorData;
    }

    /**
     * 重新发送数据给医生
     * @throws IOException
     */
    @Override
    public void reloadSend() throws IOException {
        //获取token
        String token = this.getToken(appid, corpsecret);
        // 遍历拿到所有医生的id 取出发送人
        List<RightMsg> rightMsg = new ArrayList<>();
        List<WrongMsg> wrongMsg = new ArrayList<>();
        // 遍历拿到所有医生的id 取出发送人
        List<String> doctorListId = new ArrayList<>();
        // reloadData中的key值,即对应的id
        Set<String> stringObject = reloadData.keySet();
        for (String reloadDoctorId : stringObject) {
            String reloadJson = JSON.toJSONString(doctorData.get(reloadDoctorId));
            if (StringUtils.equals("null",reloadJson)) {
                // id不存在
                log.info("id不存在,为新数据："+String.valueOf(reloadDoctorId));
            } else {
                log.info("已经存在的id："+String.valueOf(reloadDoctorId));
                // 去除已经存在的id
                doctorListId.add(reloadDoctorId);
            }
        }

        for (String id : doctorListId) {
            // 移除缓存中的数据
            reloadData.remove(id);
        }
        // 发送消息部分
        for (Map.Entry<String, Object> stringObjectEntry : reloadData.entrySet()) {
            // 重新将Map中数据转化为对象
            Doctor reloadDoctor = (Doctor) stringObjectEntry.getValue();
            // 遍历后的id
            String reloadDoctorId = reloadDoctor.getId();
            // 根据此时的id发送消息
            try {
                String msg = this.sendMsg(token, reloadDoctorId);
                log.info(msg);
                // 转化json对象
                JSONObject jsonMsg = JSONObject.parseObject(msg);
                String code = jsonMsg.getString("errcode");
                if (StringUtils.equals(code,"0")) {
                    // 成功  记录成功人KEY  记录一下成功文件   内存属性发送标记
                    RightMsg rightData = new RightMsg();
                    rightData.setId(reloadDoctorId);
                    rightMsg.add(rightData);
                    // 添加进入缓存
                    doctorData.put(reloadDoctorId,reloadDoctor);
                } else {
                    // 失败  记录失败人KEY  记录一下失败文件
                    WrongMsg wrongData = new WrongMsg();
                    wrongData.setId(reloadDoctorId);
                    wrongMsg.add(wrongData);
                }
            } catch (Exception e) {
                log.error("服务器出错了 (ó﹏ò｡)", e);
            }
        }
//        log.info("再次成功发送人的信息："+ String.valueOf(rightMsg));
//        log.info("再次失败发送人的id："+ String.valueOf(wrongMsg));
        // 将再次失败发送的id写入Excel
        String wrongFilename = reloadWrongUrl;
        ExcelWriter excelWriterWrong = EasyExcel.write(wrongFilename, WrongMsg.class).build();
        WriteSheet writeSheetWrong = EasyExcel.writerSheet("reload发送失败医生id").build();
        excelWriterWrong.write(wrongMsg,writeSheetWrong);
        excelWriterWrong.finish();
        log.info(String.valueOf(rightMsg));
        log.info(String.valueOf(wrongMsg));
    }

    /**
     * 获取成功发送的医生信息放入Excel中
     */
    @Override
    public void getSuccessExcel() {
        List<Doctor> doctorList = new ArrayList<>();
        for (Map.Entry<String, Object> stringObjectEntry : doctorData.entrySet()) {
            // 重新将Map中数据转化为对象
            Doctor doctor = (Doctor) stringObjectEntry.getValue();
            doctorList.add(doctor);
        }
        log.info("发送成功的医生信息{}"+String.valueOf(doctorList));
        // 将成功发送人的数据写入Excel
        String sendSuccessExcelUrl = sendSuccessExcel;
        ExcelWriter excelWriterRight = EasyExcel.write(sendSuccessExcelUrl, Doctor.class).build();
        WriteSheet writeSheetRight = EasyExcel.writerSheet("发送成功医生信息").build();
        excelWriterRight.write(doctorList,writeSheetRight);
        excelWriterRight.finish();
    }
}

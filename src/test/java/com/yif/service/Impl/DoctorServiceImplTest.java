package com.yif.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.yif.entity.Doctor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Yif
 */
@Slf4j
@SpringBootTest
public class DoctorServiceImplTest {

    private static Map<String, Object> doctorData;

    /**
     * 开启项目读取excel数据
     */
    @Test
    public void initDocetorData(){
        doctorData = readDoctors2();
        Set<Map.Entry<String, Object>> entries = doctorData.entrySet();
        log.info("初始化数据："+String.valueOf(entries));
    }

    static String fileName = "C:/Users/admin/Desktop/Api/医师节数据样例.xlsx";

    public static Map<String, Object> readDoctors2() {
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

    public static Map<String, Object> readDoctors() {
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

    public static Doctor findDoctor(String id) throws ParseException {
        Doctor doctorNew = new Doctor();
        List<Doctor> allDoctor = findAllDoctor();
        for (Doctor doctor : allDoctor) {
            if (StringUtils.equals(doctor.getId(),id)) {
                if(!StringUtils.isEmpty(doctor.getMaxTimeOperations())){
                    doctor.setMaxTimeOperations(doctor.getMaxTimeOperations().replace("分钟",""));
                }
                // 格式手术时长最长日期
//                if (doctor.getMaxDate()!=null)
                if (!StringUtils.isEmpty(doctor.getMaxDate()))
                {
                    if (doctor.getMaxDate().contains("/")) {
                        String maxDate = doctor.getMaxDate();
                        doctor.setMaxDate(formatDate(maxDate));
                    }
                }
                // 格式手术最长结束时间
//                if (doctor.getLatestTimeOperations()!=null)
                if (!StringUtils.isEmpty(doctor.getLatestTimeOperations())){
                    if (doctor.getLatestTimeOperations().contains("/")) {
                        String latestTimeOperations = doctor.getLatestTimeOperations();
                        doctor.setLatestTimeOperations(formatDate2(latestTimeOperations));
                    }
                }
                // 格式医生生日
                if (doctor.getBirthDate()!=null) {
                    if (doctor.getBirthDate().contains("-")) {
                        String birthDate = doctor.getBirthDate();
                        doctor.setBirthDate(formatDate3(birthDate));
                    }
                }
                BeanUtils.copyProperties(doctor,doctorNew);
            }
        }
        return doctorNew;
    }

    /**
     * 查询所有医生信息
     * @return
     */
    public static List<Doctor> findAllDoctor() {
        List<Doctor> doctors = new ArrayList<>();
        for (Map.Entry<String, Object> doctor : doctorData.entrySet()) {
            String jsonString = JSON.toJSONString(doctor.getValue());
            Doctor doctor1 = JSON.parseObject(jsonString, Doctor.class);
            doctors.add(doctor1);
        }
        return doctors;
    }

    /**
     * 转化格式 yyyy-MM-dd
     * @param date
     * @return
     * @throws ParseException
     */
    public static String formatDate(String date) throws ParseException {
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
    public static String formatDate2(String date) throws ParseException {
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
    public static String formatDate3(String date) throws ParseException {
        String replaceDate = date.replaceAll("/", "-");
        Date newDate = new SimpleDateFormat("MM-dd").parse(replaceDate);
        String now = new SimpleDateFormat("M月d日 ").format(newDate);
        return now;
    }

    public static ModelAndView findDoctorById(Model model, String id) throws ParseException {
        Doctor doctor = findDoctor("5063");
        model.addAttribute("doctors",doctor);
        return new ModelAndView ("content");
    }

    public static void main(String[] args) throws Exception {
//        initDocetorData();
        List keyList = new ArrayList<>();
        for (String key : doctorData.keySet()) {
            keyList.add(key);
        }
//        log.info(String.valueOf(doctorById));
    }


}
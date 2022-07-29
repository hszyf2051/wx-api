package com.yif.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.yif.entity.Doctor;
import com.yif.service.IDoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
@Slf4j
@Service
public class DoctorServiceImpl implements IDoctorService {

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
}

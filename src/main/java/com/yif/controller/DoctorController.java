package com.yif.controller;

/**
 * @author admin
 */

import com.yif.entity.Doctor;
import com.yif.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DoctorController {

    @Autowired
    private IDoctorService doctorService;

    @RequestMapping("/getDoctors")
    public String getDoctors(Model model) {
        List<Doctor> doctors = doctorService.readDoctors();
        model.addAttribute("doctors",doctors);
        return "index";
    }

    @GetMapping("/findDoctorById")
    public String findDoctorById(Model model,@RequestParam String id) {
        List<Doctor> doctors = doctorService.findDoctorById(id);
        model.addAttribute("doctors",doctors);
        return "index";
    }

//    @GetMapping("/testHutu")
//    public void readAll() {
//        ExcelReader reader = ExcelUtil.getReader("C:/Users/admin/Desktop/Api/医师节数据样例.xlsx");
//        List<List<Object>> readAll = reader.read();
//        System.out.println(readAll);
//    }
}

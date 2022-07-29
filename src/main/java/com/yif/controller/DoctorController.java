package com.yif.controller;

/**
 * @author admin
 */

import com.yif.entity.Doctor;
import com.yif.service.IDoctorService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@Api(tags = "Api-Doctor")
public class DoctorController {

    @Autowired
    private IDoctorService doctorService;

    /**
     * 查找所有医生
     * @param model
     * @return
     */
    @RequestMapping("/getDoctors")
    public String getDoctors(Model model) {
        List<Doctor> doctors = doctorService.readDoctors();
        model.addAttribute("doctors",doctors);
        return "index";
    }

    /**
     * 根据id查找对应的医生
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/findDoctorById")
    public String findDoctorById(Model model,@RequestParam String id) {
        List<Doctor> doctors = doctorService.findDoctorById(id);
        model.addAttribute("doctors",doctors);
        return "index";
    }
}

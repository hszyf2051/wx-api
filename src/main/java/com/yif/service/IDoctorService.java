package com.yif.service;

import com.yif.entity.Doctor;

import java.util.List;

/**
 * @author yif
 */
public interface IDoctorService {
    public List<Doctor> readDoctors();

    public List<Doctor> findDoctorById(String id);
}

package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.Doctors;
import com.awsmu.exception.AwsmuException;

public interface DoctorDao {
			
	/**
	 * function return total number doctors count
	 */
	public  int getDoctorsCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * function return list of doctors
	 */
	public  List<Doctors> getDoctorsList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException;
		
	/**
	 * get doctor detail
	 */
	public  Doctors getDoctorById(String doctorId) throws AwsmuException;
	
	/**
	 * function save doctor detail
	 */
	public  void saveDoctor(Doctors doctor) throws AwsmuException;
	
	/**
	 * function to delete Doctor
	 */
	public  void deleteDoctorById(String doctorId) throws AwsmuException;
	
}

package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.UserDoctors;
import com.awsmu.exception.AwsmuException;

public interface UserDoctorDao {
			
	/**
	 * function return total number User's doctors count
	 */
	public  int getUserDoctorsCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * function return list of User's doctors
	 */
	public  List<UserDoctors> getUserDoctorsList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException;
		
	/**
	 * get User's doctor detail
	 */
	public  UserDoctors getUserDoctorById(String doctorId) throws AwsmuException;
	
	/**
	 * function save User's doctor detail
	 */
	public  void saveUserDoctor(UserDoctors userDoctor) throws AwsmuException;
	
	/**
	 * function to delete User's Doctor
	 */
	public  void deleteUserDoctorById(String doctorId) throws AwsmuException;
	
}

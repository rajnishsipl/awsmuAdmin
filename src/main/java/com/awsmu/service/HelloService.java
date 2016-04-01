package com.awsmu.service;

import java.util.Map;

import com.awsmu.model.GridResponse;

public interface HelloService {
	/**
	 * get planner group post list
	 */
	public  GridResponse getPlannerGroupPosts(String problemId,Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);
}

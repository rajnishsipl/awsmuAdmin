package com.awsmu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.awsmu.builder.PostBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.PostDao;
import com.awsmu.entity.Posts;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PostsModel;
import com.awsmu.util.Utils;
import com.awsmu.validator.PostValidator;
import com.google.gson.Gson;
@Service
public class HelloServiceImpl implements HelloService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	/**
	 * Injecting postDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "PostDao")
	private PostDao postDao;
	
	/**
	 * Injecting AttributesDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "AttributesDao")
	private AttributesDao attributesDao;

	@Override
	public GridResponse getPlannerGroupPosts(String problemId,
			Integer skipRecord, Integer skipRecordFreq, Integer page,
			String sortBy, String sortOrder, Map<Object, Object> filters) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

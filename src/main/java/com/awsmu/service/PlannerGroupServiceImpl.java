package com.awsmu.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.awsmu.entity.PlannerGroupMember;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
@Service(value = "PlannerGroupService1")
public class PlannerGroupServiceImpl implements PlannerGroupService {

	@Override
	public GridResponse getPlannerGroupMembers(String problemId,
			Integer skipRecord, Integer skipRecordFreq, Integer page,
			String sortBy, String sortOrder, Map<Object, Object> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GridResponse getPlannerGroupPosts(String problemId,
			Integer skipRecord, Integer skipRecordFreq, Integer page,
			String sortBy, String sortOrder, Map<Object, Object> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxResponse getPlannerGroupDetail(String problemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxResponse getPlannerGroupTopMember(String problemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxResponse submitPlannerGroupEdit(String plannerGroupId,
			PlannerGroupMember plannerGroupModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxResponse activeInactivePlannerGroup(String plannerGroupId,
			int status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxResponse getPlannerGroupDetail(String problemId,
			String problemId2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String makeDatabaseChanges() {
		// TODO Auto-generated method stub
		return null;
	}

}

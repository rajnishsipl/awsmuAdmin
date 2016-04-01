package com.awsmu.builder;

import com.awsmu.config.Properties;
import com.awsmu.entity.PlannerGroupMember;
import com.awsmu.model.PlannerGroupMemberModel;
import com.awsmu.model.UserValuesModel;

public class PlannerGroupMemberBuilder {
	public PlannerGroupMemberModel fromEntityToModel(PlannerGroupMember plannerGroupMemberRow) {
		PlannerGroupMemberModel plannerGroupModel = new PlannerGroupMemberModel();
		plannerGroupModel.set_id(plannerGroupMemberRow.get_id());
		plannerGroupModel.setCreatedDate(plannerGroupMemberRow.getCreatedDate());
		plannerGroupModel.setIsActive(plannerGroupMemberRow.getIsActive());
		plannerGroupModel.setProblemId(plannerGroupMemberRow.getProblemId());
		plannerGroupModel.setPointsEarned(plannerGroupMemberRow.getPointsEarned());
		plannerGroupModel.setUpdatedDate(plannerGroupMemberRow.getUpdatedDate());
		UserValuesModel uvm = new UserValuesModel(); 
		
		uvm.setDisplayName(plannerGroupMemberRow.getUser().getDisplayName());
		String profilePic = plannerGroupMemberRow.getUser().getImage() == "" ? "profile_no_pic.png": plannerGroupMemberRow.getUser().getImage();
		int intIndex = profilePic.indexOf("http");
		if (intIndex == -1) {
			profilePic = Properties.AMAZON_PROFILE_PIC_URL+ profilePic;
		}
		uvm.setImage(profilePic);		
		uvm.setUserId(plannerGroupMemberRow.getUser().getUserId());
		uvm.setUserInfo(plannerGroupMemberRow.getUser().getUserInfo());
		uvm.setUsername(plannerGroupMemberRow.getUser().getUsername());
		uvm.setUserRole(plannerGroupMemberRow.getUser().getUserRole());
		plannerGroupModel.setUser(uvm);
		
		return plannerGroupModel;
	}

}

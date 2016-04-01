package com.awsmu.builder;

import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import com.awsmu.config.Properties;
import com.awsmu.entity.UserMessage;
import com.awsmu.entity.UserValues;
import com.awsmu.model.UserMessageModel;
import com.awsmu.model.UserValuesModel;
import com.awsmu.util.Utils;

public class MessageBuilder {
	/**
	 * convert message entity to message model before sending it to view
	*/
	public UserMessageModel fromEntityToModel(UserMessage userMessage){		
		UserMessageModel messageModel = new UserMessageModel();
		messageModel.set_id(userMessage.get_id());
		messageModel.setChainId(userMessage.getChainId());
		messageModel.setCreatedDate(userMessage.getCreatedDate());
		messageModel.setIsActive(userMessage.getIsActive());
		messageModel.setIsRead(userMessage.getIsRead());
		messageModel.setUpdatedDate(userMessage.getUpdatedDate());
		PrettyTime preetyObj = new PrettyTime();
		messageModel.setPreetyTime(preetyObj.format(userMessage.getUpdatedDate()));
		messageModel.setMessage(userMessage.getMessage());
		
		UserValuesModel uvmFrom = new UserValuesModel();
		uvmFrom.setDisplayName(userMessage.getFromUser().getDisplayName());
		
		String profilePicFrom = Utils.checkEmpty(userMessage.getFromUser().getImage()) ? "profile_no_pic.png"
				: userMessage.getFromUser().getImage();
		if (profilePicFrom.indexOf("http") == -1) {
			profilePicFrom = Properties.AMAZON_PROFILE_PIC_URL
					+ profilePicFrom;
		}
		uvmFrom.setImage(profilePicFrom);
		uvmFrom.setUserId(userMessage.getFromUser().getUserId());
		uvmFrom.setUserInfo(userMessage.getFromUser().getUserInfo());
		uvmFrom.setUsername(userMessage.getFromUser().getUsername());
		uvmFrom.setUserRole(userMessage.getFromUser().getUserRole());
		messageModel.setFromUser(uvmFrom);
		
		
		
		
		UserValuesModel uvmTo = new UserValuesModel();
		uvmTo.setDisplayName(userMessage.getToUser().getDisplayName());
		
		String profilePicTo = Utils.checkEmpty(userMessage.getToUser().getImage()) ? "profile_no_pic.png"
				: userMessage.getToUser().getImage();
		
		if (profilePicTo.indexOf("http") == -1) {
			profilePicTo = Properties.AMAZON_PROFILE_PIC_URL
					+ profilePicTo;
		}
		uvmTo.setImage(profilePicTo);
		uvmTo.setUserId(userMessage.getToUser().getUserId());
		uvmTo.setUserInfo(userMessage.getToUser().getUserInfo());
		uvmTo.setUsername(userMessage.getToUser().getUsername());
		uvmTo.setUserRole(userMessage.getToUser().getUserRole());
		
		messageModel.setToUser(uvmTo);
		
		return messageModel;
	}
	/**
	 * convert message model to user message entity sending it to view
	*/
	public UserMessage fromModelToEntityTo(UserMessageModel uMM){
		UserMessage userMessage = new UserMessage();
		userMessage.setChainId(uMM.getChainId());
		userMessage.setIsActive(1);
		userMessage.setIsDeleted(0);
		userMessage.setIsRead(0);
		userMessage.setMessage(uMM.getMessage());
		Date now = new Date();
		userMessage.setCreatedDate(now);
		userMessage.setUpdatedDate(now);
		
		UserValues fromUser = new UserValues();
		fromUser.setDisplayName(uMM.getFromUser().getDisplayName());
		fromUser.setImage(uMM.getFromUser().getImage());
		fromUser.setUserId(uMM.getFromUser().getUserId());
		fromUser.setUserInfo(uMM.getFromUser().getUserInfo());
		fromUser.setUsername(uMM.getFromUser().getUsername());
		fromUser.setUserRole(uMM.getFromUser().getUserRole());
		
		userMessage.setFromUser(fromUser);
		
		UserValues toUser = new UserValues();
		toUser.setDisplayName(uMM.getToUser().getDisplayName());
		toUser.setImage(uMM.getToUser().getImage());
		toUser.setUserId(uMM.getToUser().getUserId());
		toUser.setUserInfo(uMM.getToUser().getUserInfo());
		toUser.setUsername(uMM.getToUser().getUsername());
		toUser.setUserRole(uMM.getToUser().getUserRole());
		
		userMessage.setDeletedBy(null);
		
		userMessage.setToUser(toUser);
		
		
		return userMessage;
	}
}

package com.awsmu.builder;


import com.awsmu.entity.Friends;

import com.awsmu.model.FriendsModel;

public class FriendsBuilder {
	/**
	 * Convert model to entity 
	 */
	public Friends fromModelToEntity(FriendsModel friendsModel){
		Friends friends = new Friends();
		friends.set_id(friendsModel.get_id());
		friends.setAcceptDate(friendsModel.getAcceptDate());
		friends.setBlockDate(friendsModel.getBlockDate());
		friends.setCreatedDate(friendsModel.getCreatedDate());
		friends.setDeclineDate(friendsModel.getDeclineDate());
		friends.setFromUser(friendsModel.getFromUser());
		friends.setRequestDate(friendsModel.getRequestDate());
		friends.setStatus(friendsModel.getStatus());
		friends.setToUser(friendsModel.getToUser());
		friends.setUpdatedDate(friendsModel.getUpdatedDate());
		return friends;
	}
	
	
	/**
	 * Convert entity to model 
	 */
	public FriendsModel fromEntityToModel(Friends friends){
		
		FriendsModel friendsModel = new FriendsModel();
		friendsModel.set_id(friends.get_id());
		friendsModel.setAcceptDate(friends.getAcceptDate());
		friendsModel.setBlockDate(friends.getBlockDate());
		friendsModel.setCreatedDate(friends.getCreatedDate());
		friendsModel.setDeclineDate(friends.getDeclineDate());
		friendsModel.setFromUser(friends.getFromUser());
		friendsModel.setRequestDate(friends.getRequestDate());
		friendsModel.setStatus(friends.getStatus());
		friendsModel.setToUser(friends.getToUser());
		friendsModel.setUpdatedDate(friends.getUpdatedDate());
		return friendsModel;
	}
}

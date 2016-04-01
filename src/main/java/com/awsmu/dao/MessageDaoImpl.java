package com.awsmu.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.awsmu.config.Properties;
import com.awsmu.entity.UserMessage;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.UserMessageAggregation;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
@Repository(value="MessageDao")
public class MessageDaoImpl implements MessageDao {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	@Override
	public List<UserMessageAggregation> getRecentConversaction(Integer skipRecord,
			Integer skipFreq, Integer page, String sortBy,
			String sortOrder, List<Object> searchList) throws AwsmuException {

		
		Criteria condition = Criteria.where("isActive").is(1);
		
		condition = this.addSearchFilter(condition, searchList);

		Aggregation query = newAggregation(
				match(condition),
				group("chainId").last("message").as("message").last("isRead")
						.as("isRead").last("createdDate").as("createdDate")
						.last("chainId").as("chainId").last("deletedBy")
						.as("deletedBy").last("toUser.userId").as("toUserId")
						.last("toUser.username").as("toUserUserName")
						.last("toUser.userRole").as("toUserRole")
						.last("toUser.displayName").as("toUserDisplayName")
						.last("toUser.image").as("toUserImage")
						.last("fromUser.userId").as("fromUserUserId")
						.last("fromUser.username").as("fromUserUserName")
						.last("fromUser.userRole").as("fromUserRole")
						.last("fromUser.displayName").as("fromUserDisplayName")
						.last("fromUser.image").as("fromUserImage"),
				sort(Sort.Direction.DESC, "createdDate"), skip(skipRecord),limit(skipFreq)
		);
				
		System.out.println(query.toString());
		
		AggregationResults<UserMessageAggregation> groupResults = mongoTemplate
				.aggregate(query, UserMessage.class,
						UserMessageAggregation.class);		
		return groupResults.getMappedResults();
	}

	/*function add search parameters as per user request*/
	public Criteria addSearchFilter(Criteria condition,List<Object> searchList) {
		if(searchList!=null) {
			List<String> boolField = new ArrayList<String>();
			boolField.add("isActive");
			boolField.add("isRead");
			List<Object> filterList =  searchList;			   
		   for (Object rows : filterList) {						   
			   Map<String, String> row = (Map<String, String>) rows;					   
			   if(boolField.indexOf(row.get("field").toString())!=-1) {
				   condition.and(row.get("field").toString()).is(Integer.parseInt(row.get("data")));					  
			   } 
			   else if(row.get("field").toString().equals("_id")){
				   condition.and(row.get("field").toString()).is(row.get("data"));
			   }
			   else {
				   condition.and(row.get("field").toString()).regex(WordUtils.capitalize(row.get("data").toString()));
			   }				   
		   }						   
		}	
		return condition;
	}
	
	/*function add search parameters as per user request*/
	public DBObject addSearchFilterDBObj(DBObject query,List<Object> searchList) {
		if(searchList!=null) {
			List<String> boolField = new ArrayList<String>();
			boolField.add("isActive");
		   List<Object> filterList =  searchList; 
		   for (Object rows : filterList) {						   
			   Map<String, String> row = (Map<String, String>) rows;					   
			   if(boolField.indexOf(row.get("field").toString())!=-1) {
				   query.put(row.get("field").toString(), Integer.parseInt(row.get("data")));					  
			   } 
			   else if(row.get("field").toString().equals("_id")){
				   query.put(row.get("field").toString(), new BasicDBObject("$gt",row.get("$regex").toString()));
			   }
			   else {				  
				   query.put(row.get("field").toString(), "/"+row.get("data").toString()+"/");				  
			   }				   
		   }						   
		}	
		return query;
	}
	
	@Override
	public int getRecentConversactionCount(List<Object> searchList)
			throws AwsmuException {		
		DBObject query = new BasicDBObject();
		query = this.addSearchFilterDBObj(query, searchList);
		DBCollection collectionObj = mongoTemplate.getCollection("userMessage");	
		return collectionObj.distinct("chainId",query).size();
	}

	@Override
	public List<UserMessage> getMessageThread(String chainId, int skipRecord, int skipFreq) throws AwsmuException {
		Query query = new Query(Criteria.where("chainId").in(chainId));
		query.limit(skipFreq);
		query.skip(skipRecord);
		query.with(new Sort(Sort.Direction.DESC, "createdDate"));
		return mongoTemplate.find(query, UserMessage.class);		
	}
	@Override
	public String saveMessage(UserMessage userMessage) throws AwsmuException {		
		try{
			mongoTemplate.insert(userMessage, "userMessage");
			return userMessage.get_id();
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}		
	}
}

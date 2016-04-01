package com.awsmu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.awsmu.config.Properties;
import com.awsmu.entity.PlannerGroup;
import com.awsmu.entity.PlannerGroupMember;
import com.awsmu.entity.Posts;
import com.awsmu.entity.Problem;
import com.awsmu.exception.AwsmuException;
@Repository(value = "PlannerGroupDao")
public class PlannerGroupDaoImpl implements PlannerGroupDao {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true) 
	private MongoTemplate mongoTemplate;
	@Override
	public List<PlannerGroupMember> getPlannerGroupMember(String problemId,Integer skipRecord,
			Integer skipFreq, Integer page, String sortBy, String sortOrder,
			List<Object> searchList) throws AwsmuException {
		List<PlannerGroupMember> postList;
		try{		
			Query query = new Query();
			query.addCriteria(Criteria.where("problemId").is(problemId));
			query.limit(skipFreq);		
			query.skip(skipRecord);
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			
			query = addSearchFilter(query,searchList);			
			postList =  mongoTemplate.find(query, PlannerGroupMember.class);
		}
		catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	    } 
		return postList;		
	}

	@Override
	public PlannerGroupMember getPlannerGroupById(String plannerGroupId)
			throws AwsmuException {
		
		return null;
	}

	@Override
	public int getPlannerGroupMemberCount(String problemId,List<Object> searchList)
			throws AwsmuException {		
		int totalCount=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("problemId").is(problemId));
			System.out.println(query.toString());
			totalCount =  mongoTemplate.find(query, PlannerGroupMember.class).size();
			System.out.println("after"+query.toString());
		}
		catch(Exception e){	    	
			System.out.println(e.getMessage());
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }		
		return totalCount;
	}

	
	@Override
	public List<Posts> getPlannerGroupPosts(String problemId,Integer skipRecord, Integer skipFreq,
			Integer page, String sortBy, String sortOrder,
			List<Object> searchList) throws AwsmuException {
		List<Posts> postList;
		try{		
			Query query = new Query();		 
			query.limit(skipFreq);		
			query.skip(skipRecord);
			query.addCriteria(Criteria.where("postFor.typeId").is(problemId));
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			
			query = addSearchFilter(query,searchList);			
			postList =  mongoTemplate.find(query, Posts.class);
		}
		catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	    } 
		return postList;
	}

	@Override
	public int getPlannerGroupPostCount(String problemId,List<Object> searchList) throws AwsmuException {
		int totalPosts=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("postFor.typeId").is(problemId));
			totalPosts =  mongoTemplate.find(query, Posts.class).size();
		}
		catch(Exception e){
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }		
		return totalPosts;
	}
	
	
	@Override
	public void submitPlannerGroupEdit(String plannerGroupId,
			PlannerGroupMember plannerGroup) throws AwsmuException {
		
		
	}

	@Override
	public void activeInactivePlannerGroup(String plannerGroupId, int status)
			throws AwsmuException {
		
		
	}

	@Override
	public void inDecPostCommentCount(String postId, int status)
			throws AwsmuException {
		
		
	}

	@Override
	public void inDecPostLikeCount(String postId, int status)
			throws AwsmuException {
		
		
	}
	/*function add search parameters as per user request*/
	@SuppressWarnings("rawtypes")
	public Query addSearchFilter(Query query,List<Object> searchList) {
		if(searchList!=null) {
			List<String> boolField = new ArrayList<String>();
			boolField.add("isActive");
			boolField.add("pointsEarned");
			
		   List<Object> filterList =  searchList;			   
		   for (Object rows : filterList) {
			   
			   @SuppressWarnings("unchecked")
			   Map<String, String> row = (Map) rows;					   
			   if(boolField.indexOf(row.get("field").toString())!=-1 && row.get("data").matches("[0-9]+")) {
				   query.addCriteria(Criteria.where(row.get("field").toString()).is(Integer.parseInt(row.get("data"))));					  
			   } 
			   else if(row.get("field").toString().equals("_id")){
				   query.addCriteria(Criteria.where(row.get("field").toString()).is(row.get("data")));
			   }
			   else {
				   query.addCriteria(Criteria.where(row.get("field").toString()).regex(WordUtils.capitalize(row.get("data").toString())));
			   }				   
		   }						   
		}	
		return query;
	}

	@Override
	public List<PlannerGroupMember> getPlannerGroupTopMembers(String problemId)
			throws AwsmuException {
		// query to search id
		Query query = new Query(Criteria.where("problemId").is(problemId)).limit(10);
		query.addCriteria(Criteria.where("isActive").is(1));
		query.with(new Sort(Sort.Direction.DESC, "pointsEarned"));		
		return mongoTemplate.find(query, PlannerGroupMember.class);
	}
	
	/*creating function to transfer data */
	public static void main(String args[]){				
		// ApplicationContext context =   new ClassPathXmlApplicationContext("servlet-context.xml");		
		// MongoTemplate mt = (MongoTemplate) context.getBean(MongoTemplate.class);
		// updatePlannerGroupmem(mt);
	}
	
	public static void transferDataProblemToPlannerGroup(MongoTemplate mt){
		Query query = new Query();		
		//query.addCriteria(Criteria.where("isActive").is(1));
		//query.with(new Sort(Sort.Direction.DESC, "pointsEarned"));		
		List<Problem> list = mt.find(query, Problem.class);
		for(Problem p: list) {
			PlannerGroup pg= new PlannerGroup();
			pg.setBanner(p.getBanner());
			pg.setCreatedDate(p.getCreatedDate());
			pg.setDescription(pg.getDescription());
			pg.setIcon(p.getIcon());
			pg.setIsActive(p.getIsActive());
			pg.setIsDeleted(p.getIsDeleted());
			pg.setName(p.getName());
			pg.setProblemId(p.get_id());
			pg.setTitle(p.getTitle());
			pg.setTrends(p.getTrends());
			pg.setUpdatedDate(p.getUpdatedDate());			
			mt.insert(pg);
		}
	}
	
	/*Its one time code to */
	public static void updatePlannerGroupmem(MongoTemplate mt) {
		Query query = new Query();		
		List<PlannerGroup> list = mt.find(query, PlannerGroup.class);
		for(PlannerGroup p: list) {
			System.out.println(p.get_id());
			Query query1 = new Query();			
			query1.addCriteria(Criteria.where("problemId").is(p.getProblemId()));	
			Update update = new Update();
			update.set("plannerGroupId", p.get_id());
			//mt.updateFirst(query1, update, PlannerGroupMember.class);
			mt.updateMulti(query1, update, PlannerGroupMember.class);
		}
	}
	
	public void makeDatabaseChanges() throws AwsmuException {
		System.out.println("Hello makeDatabaseChanges");
	}
}

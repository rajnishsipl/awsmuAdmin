package com.awsmu.dao;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.awsmu.config.Properties;
import com.awsmu.entity.Attributes;
import com.awsmu.entity.City;
import com.awsmu.entity.Nationality;
import com.awsmu.entity.Profession;
import com.awsmu.entity.Specialties;
import com.awsmu.entity.DegreeCourses;
import com.awsmu.entity.Trends;
import com.awsmu.exception.AwsmuException;


/**
 * User Attributes implementation
 */
@Repository(value = "AttributesDao")
public class AttributesDaoImpl implements AttributesDao {
	
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	/**
	 * natioanality list
	 */
	@Override
	public Nationality getNatioanlityList() throws AwsmuException {
	
		Nationality nationality;
		try{		
			Query query = new Query(Criteria.where("attr").is("Country"));
			nationality =  mongoTemplate.findOne(query, Nationality.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	     }
		
		return nationality;
	}
	
	/**
	 * profession list
	 */
	@Override
	public Profession getProfessionList() throws AwsmuException {
		Profession profession;
		try{		
			Query query = new Query(Criteria.where("attr").is("Profession"));
			profession =  mongoTemplate.findOne(query, Profession.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	     }
		
		return profession;
	}
	
	/**
	 * specialist list
	 */
	@Override
	public Specialties getSpecialtiesList() throws AwsmuException {
		Specialties specialties;
		try{		
			Query query = new Query(Criteria.where("attr").is("Speciality"));
			specialties =  mongoTemplate.findOne(query, Specialties.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	     }
		
		return specialties;
	}
	
	/**
	 * degree list
	 */
	@Override
	public DegreeCourses getDegreeCoursesList() throws AwsmuException {
		DegreeCourses degreeCourses;
		try{		
			Query query = new Query(Criteria.where("attr").is("degreeCourses"));
			degreeCourses =  mongoTemplate.findOne(query, DegreeCourses.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	     }
		
		return degreeCourses;
	}
	
	/**
	 * cities list
	 */
	@Override
	public City getCitiesList(String country) throws AwsmuException {
		City cities;
		try{		
			Query query = new Query(Criteria.where("country").is(country));
			cities =  mongoTemplate.findOne(query, City.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	     }
		
		return cities;
	}
	
	
	
	
	
	/**
	 * get Attributes grid
	 */	
	@Override
	public List<Attributes> getAttributesGrid(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<Attributes> attributesList;
		
		try{
			Query query = new Query();		 
			query.limit(skipPostFreq);		
			query.skip(skipPostRecord);
			
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			
			query = addSearchFilter(query,searchList);
					
			attributesList =  mongoTemplate.find(query, Attributes.class);
		}
	    catch(Exception e){
	   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return attributesList;
	}
	
	/**
	 * get user based on the pagination criteria
	 */
	
	/*function add search parameters as per user request*/
	public Query addSearchFilter(Query query,List<Object> searchList) {
		if(searchList!=null) {
			List<String> boolField = new ArrayList<String>();
			boolField.add("isActive");
			boolField.add("isDeleted");
		   List<Object> filterList =  searchList;			   
		   for (Object rows : filterList) {						   
			   Map<String, String> row = (Map) rows;					   
			   if(boolField.indexOf(row.get("field").toString())!=-1) {
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
	
	
	/**
	 * get attributes count based on the pagination criteria
	 */
	@Override
	public  int getAttributesCount(List<Object> searchList) throws AwsmuException{
		int total=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);	
			total =  mongoTemplate.find(query, Attributes.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return total;
	}
		
	/**
	 * trends list
	 */
	@Override
	public List<Trends> getTrendsList() throws AwsmuException {
		List<Trends> trends;
		try{		
			Query query = new Query(Criteria.where("isActive").is(1));
			trends =  mongoTemplate.find(query, Trends.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	     }
		
		return trends;
	}
	
	
	/**
	 * update country 
	 */
	@Override
	public void updateCountry( Nationality nationality) throws AwsmuException{
		try{
		   mongoTemplate.save(nationality, "attributes");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	
	/**
	 * update degree courses 
	 */
	@Override
	public void updateDegreeCourses( DegreeCourses degreeCourses) throws AwsmuException{
		try{
		   mongoTemplate.save(degreeCourses, "attributes");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	/**
	 * update and add professions 
	 */
	@Override
	public void updateProfessions( Profession professions) throws AwsmuException{
		try{
		   mongoTemplate.save(professions, "attributes");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	/**
	 * update and add specialties 
	 */
	@Override
	public void updateSpecialties( Specialties specialties) throws AwsmuException{
		try{
		   mongoTemplate.save(specialties, "attributes");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
}
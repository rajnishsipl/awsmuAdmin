package com.awsmu.builder;


import com.awsmu.config.Properties;
import com.awsmu.entity.Booklet;
import com.awsmu.model.BookletModel;


/**
 * Handles conversion between booklet model and booklet entity.
 *
 */
public class BookletBuilder {
	/**
	 * convert booklet model to booklet entity before sending it to database
	 */
	public Booklet fromModelToEntity(BookletModel bookletModel){
		Booklet booklet = new Booklet();
		
		if(bookletModel.get_id() != null)
			booklet.set_id(bookletModel.get_id());
				
		booklet.setUserId(bookletModel.getUserId());
		booklet.setTitle(bookletModel.getTitle());
		booklet.setDescription(bookletModel.getDescription());
		booklet.setFile(bookletModel.getFile());
		booklet.setFileType(bookletModel.getFileType());
		booklet.setIsDeleted(bookletModel.getIsDeleted());
		booklet.setIsActive(bookletModel.getIsActive());
		booklet.setCreatedDate(bookletModel.getCreatedDate());
		booklet.setUpdatedDate(bookletModel.getUpdatedDate());
				 
		 return booklet;
	}
	
	/**
	 * convert booklet entity to booklet model before sending it to view
	 */
	public BookletModel fromEntityToModel(Booklet booklet){
		
		BookletModel bookletModel = new BookletModel();
		
		bookletModel.set_id(booklet.get_id());
		bookletModel.setUserId(booklet.getUserId());
		bookletModel.setTitle(booklet.getTitle());
		bookletModel.setDescription(booklet.getDescription());
		
		String bookletUrl = booklet.getFile() == "" ? "": Properties.AMAZON_BOOKLET_URL+ booklet.getFile();
		bookletModel.setFileUrl(bookletUrl);
		bookletModel.setFile(booklet.getFile());
		bookletModel.setFileType(booklet.getFileType());
		bookletModel.setIsDeleted(booklet.getIsDeleted());
		bookletModel.setIsActive(booklet.getIsActive());
		bookletModel.setCreatedDate(booklet.getCreatedDate());
		bookletModel.setUpdatedDate(booklet.getUpdatedDate());
				
		return bookletModel;
	}
	
}

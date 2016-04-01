package com.awsmu.util;

import java.math.BigInteger;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Define all common function
 *
 */

public class Utils {
	/**
	 * return md5 of string
	 * 
	 * @return
	 */
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * function extracts the search parameters and returns list of maps
	 * 
	 * @return
	 */
	public static List<Object> getSearchParameter(Map<Object,Object> filters) {
		if(filters!=null) {
			for (Map.Entry<Object, Object> entry : filters.entrySet()) {
			   if(entry.getKey().equals("rules")) {
				   return  (List<Object>) entry.getValue();					   
			   }
			}
		}
		return null;
	}

	/**
	 * check empty string
	 */
	
	public static boolean checkEmpty(String str){
		if(str == null || str == ""  || str.isEmpty())
			return true;
		return false;
	}
	
	
	/**
	 * check empty string
	 */
	
	public static boolean checkRegx(String str, String regx){
		if(str == null || str == ""  || str.isEmpty() || str.equals("")){
			return false;
		}else{	
		   if(str.matches(regx))
			   return true;
		   else
			   return false;   
		}
	}
	
	/**
	 * check empty List
	 */
	
	public static boolean checkEmptyStringList(List<String> listStr){
		if(listStr !=null && listStr.size()<1)
			return true;
		return false;
	}
	
	
	/**
	 * check string length
	 */
	
	public static boolean checkStringLength(String str, int length){
		if(str != null && str.length()>length)
			return true;
		
		return false;
	}
	
	/**
	 * get date format
	 *
	 */
	public static String getDateFormat(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat("M/d/yyyy");
		String formattedDate = dateformat.format(date);
		return formattedDate;
	}
	
	public static void main(String arg[]){
		//Utils u = new Utils();
		
		if(Utils.checkRegx("sachin", "^[a-zA-Z0-9_-]{2,50}$"))
		    System.out.println("true");
		else
			System.out.println("false");
	}
	
	/*function finds url and wrap up with anchor tag*/
	public static String replaceURLs(String text) {
		String urlValidationRegex = "(https?|ftp)://(www\\d?|[a-zA-Z0-9]+)?.[a-zA-Z0-9-]+(\\:|.)([a-zA-Z0-9.]+|(\\d+)?)([/?:].*)?";
		Pattern p = Pattern.compile(urlValidationRegex);
		Matcher m = p.matcher(text);
		StringBuffer sb = new StringBuffer();
		while(m.find()){
		    String found =m.group(0); 
		    m.appendReplacement(sb, "<a target='_blank' href='"+found+"'>"+found+"</a>"); 
		}
		m.appendTail(sb);
		return sb.toString();
		}
	
}
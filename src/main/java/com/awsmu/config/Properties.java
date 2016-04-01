package com.awsmu.config;

/**
 * Define all constant
 *
 */
public class Properties {	
	
	public static final String SITE_NAME = "Admin AwsmU";
	
	// Production DB
	/*public static final String DB_STRING = "mongodb://healthcare_prod:healthcare_prod@712ece608f7e17cedffb71f0535fe00e/iad-c17-0.objectrocket.com:49020,iad-c17-1.objectrocket.com:49020/healthcare_prod";
	public static final String DB_NAME = "healthcare_prod";*/
	
	
	public static final String ADMIN_EMAIL = "admin@awsmu.com";
	public static final String CONTACT_EMAIL = "contact@awsmu.com";
	
	//local host detail
	public static final String BASE_URL = "http://localhost:8080/admin/";
	public static final String PIC_UPLOAD_PATH = System.getProperty("catalina.base")+"/wtpwebapps/awsmuAdmin/resources/profilepics/";
	//public static final String PIC_UPLOAD_PATH = "opt/sts-bundle/pivotal-tc-server-developer-3.1.0.RELEASE/base-instance/wtpwebapps/awsmudev/resources/images/profilepics/";
	public static final String BOOKLET_UPLOAD_PATH = "opt/sts-bundle/pivotal-tc-server-developer-3.1.0.RELEASE/base-instance/wtpwebapps/awsmudev/resources/booklet/";
	
	//Amazon hosting detail
	//public static final String BASE_URL = "http://www.crazywork.awsmu.com/";
	//public static final String PIC_UPLOAD_PATH = "/var/lib/tomcat8/webapps/ROOT/resources/images/profilepics/";
	//public static final String BOOKLET_UPLOAD_PATH = "/var/lib/tomcat8/webapps/ROOT/resources/booklet/";
	
	//Amazon bucket 
	public static final String AMAZON_ACCESS_KEY = "AKIAIOOO23P6Q6A7MU3Q";
	public static final String AMAZON_SECRET_KEY = "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";
	public static final String AMAZON_UPLOAD_SRC = "https://awsmuuploads.s3.amazonaws.com/";
	public static final String AMAZON_PROFILE_PIC_PATH = "awsmuuploads/profilepics";
	public static final String AMAZON_PROFILE_PIC_URL = AMAZON_UPLOAD_SRC+"profilepics/";
	public static final String AMAZON_BOOKLET_PATH = "awsmuuploads/booklets";
	public static final String AMAZON_BOOKLET_URL = AMAZON_UPLOAD_SRC+"booklets/";
	public static final String AMAZON_SITE_UPLOADS_PATH = "awsmuuploads/siteuploads";
	public static final String AMAZON_SITE_UPLOADS_URL = AMAZON_UPLOAD_SRC+"siteuploads/";
		
	//social site url
	public static final String  FACEBOOK_URL = "https://www.facebook.com/pages/AWSMU/847310775319848?skip_nax_wizard=true";
	public static final String  GOOGLE_URL = "https://plus.google.com/u/0/+AngelStein-Awsmu/posts";
	public static final String  TWITTER_URL = "https://twitter.com/u_awsm";

	// Friends status
	public static final String FRIEND_PENDING = "pending";
	public static final String FRIEND_ACCEPT = "accept";
	public static final String FRIEND_DECLINE = "decline";
	// post type
	public static final String POST_NONE = "none";
	
	public static final String POST_GROUP = "group";

	// User role
	public static final String ROLE_USER = "user";
	public static final String ROLE_ADMIN = "admin";
	public static final String ROLE_EXPERT = "expert";

	// Welcome messages
	public static final String WELCOME_MESSAGE = "Thank you very much for joining the platform."
													  + " \n I take the pleasure to welcome you on our platform.";
	// social actions
	public static final String ACTION_LIKE = "like";
	public static final String ACTION_COMMENT = "comment";
	public static final String ACTION_POST = "post";
	public static final String ACTION_SHARE = "share";
	public static final String ACTION_VIEW_PLANNER = "viewPlanner";
	
	
	/*user full for activity done*/
	public static final String ACTION_ACTIVITY = "activity";

	/*Performance calculation points */	
	public static final int POINT_POST_LIKE = 1;
	public static final int POINT_POST_COMMENT = 1;
	public static final int POINT_POST = 5;
	public static final int POINT_VIEW_PLANNER = 2;
	public static final int POINT_ACTIVITY_DONE = 2;
	/*Performance calculation points */
	
	
	/*Exception message */
	public static final String EXCEPTION_SERVICE_ERROR = "Service level exception";
	public static final String EXCEPTION_DAO_ERROR = "DAO level exception";
	public static final String EXCEPTION_DATABASE = "Database exception was occured.";
	public static final String EXCEPTION_IN_LOGIC = "Code level exception occured.";
	public static final String AMAZON_BUCKET_UPLOAD_EXCEPTION_ERROR = "Amazon bucket upload exception occured.";
	public static final String AMAZON_BUCKET_REMOVE_EXCEPTION_ERROR = "Amazon bucket remove exception occured.";
	
	public static final int DEFAULT_EXCEPTION_ERROR_CODE = 500;
	public static final int USER_AUTHENTICATION_FAILED_CODE = 401;
	public static final String USER_AUTHENTICATION_FAILED_MESSAGE = "Please login to continue";
	
	/*Log message */
	public static final String CONTROLLER_LOG = "Controller log";
	
	/*Error code */
	public static final int INTERNAL_SERVER_ERROR= 500;
	public static final int BAD_REQUEST_ERROR= 400; 
	public static final int NOT_FOUND_ERROR= 404; 
	/*Site message*/
	public static final String INVALID_LOGIN = "Incorrect email or password";
	public static final String NO_RECORD_FOUND = "No record found.";
	
	/* basic constraint */
	public static final int POST_CONTENT_CHAR_LENGTH = 200;
	
	/* Admin Id */
	public static final String ADMIN_ID = "559ec0fdd165fc17284a036b";
		
}

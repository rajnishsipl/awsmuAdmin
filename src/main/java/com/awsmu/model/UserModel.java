/**
 * 
 */
package com.awsmu.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Handles user attribute.
 *
 */
@Component("userModel")
public class UserModel {
	
	private String _id;
	
	private String name;
	private String displayName;
	private String username;
	private String password;
	private String email;
	private String dateOfBirth;
	private String gender;
	private String profession;
	private String nationality;
	private String image;
	private String country;
	private String city;
	private String verificationCode;
	private Integer isVerified;
	private Integer forgotPassword;
	private String address;
	private String phone;
	private String aboutMe;
	private String facebookId;
	private String twitterId;
	private String linkedinId;
	private String googleId;
	private String showAttributes;
	private String showPersonal;
	private String showPlanner;
	private Integer isActive;
	private String latitude;
	private String longitude;
	private Integer isDeleted;
	private Date lastLogin;
	private Date createdDate;
	private Date updatedDate;
	private String userRole;
	private String area;
	private List<String> degreeCourses;
	private List<String> specialties;
	private String facebookUrl;
	private String twitterUrl;
	private String linkedInUrl;

	
	
	public String getFacebookUrl() {
		return facebookUrl;
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}

	public String getLinkedInUrl() {
		return linkedInUrl;
	}

	public void setLinkedInUrl(String linkedInUrl) {
		this.linkedInUrl = linkedInUrl;
	}

	public List<String> getDegreeCourses() {
		return degreeCourses;
	}

	public void setDegreeCourses(List<String> degreeCourses) {
		this.degreeCourses = degreeCourses;
	}

	public List<String> getSpecialties() {
		return specialties;
	}

	public void setSpecialties(List<String> specialties) {
		this.specialties = specialties;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String get_id() {
		return _id;
	}
	
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}
	
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getVerificationCode() {
		return verificationCode;
	}
	
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	public Integer getIsVerified() {
		return isVerified;
	}
	
	public void setIsVerified(Integer isVerified) {
		this.isVerified = isVerified;
	}
	
	public Integer getForgotPassword() {
		return forgotPassword;
	}
	
	public void setForgotPassword(Integer forgotPassword) {
		this.forgotPassword = forgotPassword;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getFacebookId() {
		return facebookId;
	}
	
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getTwitterId() {
		return twitterId;
	}
	
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	
	public String getLinkedinId() {
		return linkedinId;
	}
	
	public void setLinkedinId(String linkedinId) {
		this.linkedinId = linkedinId;
	}
	
	public String getGoogleId() {
		return googleId;
	}
	
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	
	public String getShowAttributes() {
		return showAttributes;
	}

	public void setShowAttributes(String showAttributes) {
		this.showAttributes = showAttributes;
	}

	public String getShowPersonal() {
		return showPersonal;
	}

	public void setShowPersonal(String showPersonal) {
		this.showPersonal = showPersonal;
	}

	public String getShowPlanner() {
		return showPlanner;
	}

	public void setShowPlanner(String showPlanner) {
		this.showPlanner = showPlanner;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}
	
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
		
	}
		
}
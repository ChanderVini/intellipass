package com.appsec.intellipass.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * @author Chander Singh [chander (dot) singh (at) gmail (dot) com]
 * Jun 11, 2009
 */
public class UserRequestVO implements Serializable {
	private static final long serialVersionUID = -3858566855480686940L;
	
	private String sessionId;
	private String userCd;
	private int loginAttempts;
	private String loginSuccess;
	private String userLocked;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp updateDate;
	private String updatedBy;
	
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * @return the userCd
	 */
	public String getUserCd() {
		return userCd;
	}
	
	/**
	 * @param userCd the userCd to set
	 */
	public void setUserCd(String userCd) {
		this.userCd = userCd;
	}
	
	/**
	 * @return the loginAttempts
	 */
	public int getLoginAttempts() {
		return loginAttempts;
	}
	
	/**
	 * @param loginAttempts the loginAttempts to set
	 */
	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
	
	/**
	 * @return the loginSuccess
	 */
	public String getLoginSuccess() {
		return loginSuccess;
	}
	
	/**
	 * @param loginSuccess the loginSuccess to set
	 */
	public void setLoginSuccess(String loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
	
	/**
	 * @return the userLocked
	 */
	public String getUserLocked() {
		return userLocked;
	}
	
	/**
	 * @param userLocked the userLocked to set
	 */
	public void setUserLocked(String userLocked) {
		this.userLocked = userLocked;
	}
	
	/**
	 * @return the createDate
	 */
	public Timestamp getCreateDate() {
		return createDate;
	}
	
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	/**
	 * @return the updateDate
	 */
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}
	
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}

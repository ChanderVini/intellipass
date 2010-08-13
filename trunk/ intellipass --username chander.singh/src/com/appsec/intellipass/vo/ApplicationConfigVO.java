package com.appsec.intellipass.vo;

import java.io.Serializable;

/**
 * 
 * @author Chander Singh [chander (dot) singh (at) gmail (dot) com]
 * @ Created on May 10, 2009
 */
public class ApplicationConfigVO implements Serializable {
	private static final long serialVersionUID = 489175998126961433L;
	
	private int defMinLoginAtmpt;
	private int defMaxLoginAtmpt;
	private int minLoginAtmpt;
	private int maxLoginAtmpt;
	private int defLoginAtmptLock;
	private int loginAtmptLock;
	
	/**
	 * @return the defMinLoginAtmpt
	 */
	public int getDefMinLoginAtmpt() {
		return defMinLoginAtmpt;
	}
	
	/**
	 * @param defMinLoginAtmpt the defMinLoginAtmpt to set
	 */
	public void setDefMinLoginAtmpt(int defMinLoginAtmpt) {
		this.defMinLoginAtmpt = defMinLoginAtmpt;
	}
	
	/**
	 * @return the defMaxLoginAtmpt
	 */
	public int getDefMaxLoginAtmpt() {
		return defMaxLoginAtmpt;
	}
	
	/**
	 * @param defMaxLoginAtmpt the defMaxLoginAtmpt to set
	 */
	public void setDefMaxLoginAtmpt(int defMaxLoginAtmpt) {
		this.defMaxLoginAtmpt = defMaxLoginAtmpt;
	}
	
	/**
	 * @return the minLoginAtmpt
	 */
	public int getMinLoginAtmpt() {
		return minLoginAtmpt;
	}
	
	/**
	 * @param minLoginAtmpt the minLoginAtmpt to set
	 */
	public void setMinLoginAtmpt(int minLoginAtmpt) {
		this.minLoginAtmpt = minLoginAtmpt;
	}
	
	/**
	 * @return the maxLoginAtmpt
	 */
	public int getMaxLoginAtmpt() {
		return maxLoginAtmpt;
	}
	
	/**
	 * @param maxLoginAtmpt the maxLoginAtmpt to set
	 */
	public void setMaxLoginAtmpt(int maxLoginAtmpt) {
		this.maxLoginAtmpt = maxLoginAtmpt;
	}
	
	/**
	 * @return the defLoginAtmptLock
	 */
	public int getDefLoginAtmptLock() {
		return defLoginAtmptLock;
	}
	
	/**
	 * @param defLoginAtmptLock the defLoginAtmptLock to set
	 */
	public void setDefLoginAtmptLock(int defLoginAtmptLock) {
		this.defLoginAtmptLock = defLoginAtmptLock;
	}
	
	/**
	 * @return the loginAtmptLock
	 */
	public int getLoginAtmptLock() {
		return loginAtmptLock;
	}
	
	/**
	 * @param loginAtmptLock the loginAtmptLock to set
	 */
	public void setLoginAtmptLock(int loginAtmptLock) {
		this.loginAtmptLock = loginAtmptLock;
	}
}
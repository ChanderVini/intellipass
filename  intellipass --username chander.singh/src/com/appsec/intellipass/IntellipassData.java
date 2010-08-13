package com.appsec.intellipass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.appsec.intellipass.exception.IntellipassException;
import com.appsec.intellipass.util.DbConnection;
import com.appsec.intellipass.util.IntellipassUtil;
import com.appsec.intellipass.vo.ApplicationConfigVO;
import com.appsec.intellipass.vo.UserRequestVO;

/**
 * 
 * @author Chander Singh [chander (dot) singh (at) gmail (dot) com]
 * @ Created on Apr 16, 2009
 */
public class IntellipassData {
	private Logger logger = Logger.getLogger(IntellipassData.class);
	
	/**
	 * 
	 * @param request
	 * @param userId
	 * @param loginSuccess
	 * @throws IntellipassException
	 */
	public int handleUserLogin (
			HttpServletRequest request, 
			String userCd, 
			boolean loginSuccess) throws 
			IntellipassException {
		
		String sessionId = request.getSession().getId();
		int userLockStatus = -1;
		String loginsuccess = "N";
		if (loginSuccess) {
			loginsuccess = "Y";
		}
		Connection connection = null;
		try {
			connection = DbConnection.getConnection();
			connection.setAutoCommit(false);
			int loginAttempts = fetchLoginAttempts (
					connection, 
					sessionId, 
					userCd);
			if (loginAttempts > 0) {
				loginAttempts++;
				userLockStatus = processLockoutStatus (
						connection, 
						userCd, 
						sessionId, 
						loginAttempts);
				String lockUser = "N";
				if (userLockStatus == 0) {
					lockUser = "Y";
				}
				updateUserRequest (
						connection, 
						sessionId, 
						userCd, 
						loginAttempts, 
						loginsuccess, 
						lockUser);
			} else {
				loginAttempts++;
				insertUserRequest (
						connection, 
						sessionId, 
						userCd, 
						loginAttempts, 
						loginsuccess);
			}		
			connection.commit();
		} catch (SQLException sqlexp) {
			logger.error (sqlexp.getMessage(), sqlexp);
			try {
				connection.rollback();
			} catch (SQLException exp) {}
			throw new IntellipassException (sqlexp.getMessage());
		} finally {
			DbConnection.close(connection, null, null);
		}
		return userLockStatus;
	}
	
	/**
	 * 
	 * @param connection
	 * @param sessionId
	 * @param userCd
	 * @return
	 * @throws IntellipassException
	 */
	private int fetchLoginAttempts (
			Connection connection, 
			String sessionId, 
			String userCd) throws 
			IntellipassException {
		
		int loginAttempts= 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer queryBuf = new StringBuffer ("SELECT " +
					"LOGIN_ATMPTS_NBR " +
					"FROM " +
					"user_request_detail " +
					"WHERE " +
					"SESSION_ID = ? AND " +
					"USER_CD = ?");
			logger.debug ("Query: " + queryBuf.toString());
			ps = connection.prepareStatement(queryBuf.toString());
			queryBuf = null;
			
			logger.debug ("Session Id: " + sessionId);
			logger.debug ("User Cd: " + userCd);
			
			ps.setString (1, sessionId);
			ps.setString (2, userCd);
			
			rs = ps.executeQuery();
			if (rs.next()) {
				loginAttempts = rs.getInt(1);
			}
		} catch (SQLException sqlexp) {
			logger.error(sqlexp.getMessage(), sqlexp);
			throw new IntellipassException (sqlexp.getMessage());
		} finally {
			DbConnection.close (null, ps, rs);
		}
		return loginAttempts;		
	}
	 
	/**
	 * 
	 * @param connection
	 * @param sessionId
	 * @param userCd
	 * @param loginAttempts
	 * @param loginSuccess
	 * @param lockUser
	 * @throws IntellipassException
	 */
	private void updateUserRequest (
			Connection connection, 
			String sessionId, 
			String userCd, 
			int loginAttempts, 
			String loginSuccess, 
			String lockUser) throws 
			IntellipassException {
		
		logger.info ("Start updateUserRequest (" +
				"Connection, " +
				"String, " +
				"String, " +
				"int, " +
				"String");
		PreparedStatement ps = null;
		try {
			StringBuffer queryBuf = new StringBuffer ("UPDATE " +
					"user_request_detail " +
					"SET LOGIN_ATMPTS_NBR = ?, " +
					"LOGIN_SUCCESS = ?, " +
					"USER_LOCKED = ?, " +
					"UPDATE_DATE = CURRENT_TIMESTAMP, " +
					"UPDATE_USER_CD = ? " +
					"WHERE " +
					"SESSION_ID = ? AND " +
					"USER_CD = ?");
			
			logger.debug ("Query: " + queryBuf.toString());
			ps = connection.prepareStatement(queryBuf.toString());
			queryBuf = null;
			
			ps.setInt (1, loginAttempts);
			ps.setString (2, loginSuccess);
			ps.setString (3, lockUser);
			ps.setString (4, "SYSTEM");
			ps.setString (5, sessionId);
			ps.setString (6, userCd);
						
			ps.executeUpdate();
		} catch (SQLException sqlexp) {
			logger.error(sqlexp.getMessage(), sqlexp);
			throw new IntellipassException (sqlexp.getMessage());
		} finally {
			DbConnection.close(null, ps, null);
		}
		
		logger.info ("End updateUserRequest (" +
				"Connection, " +
				"String, " +
				"String, " +
				"int, " +
				"String");
	}
	
	/**
	 * 
	 * @param connection
	 * @param sessionId
	 * @param userCd
	 * @param loginAttempts
	 * @param loginSuccess
	 * @throws IntellipassException
	 */
	private void insertUserRequest (
			Connection connection, 
			String sessionId, 
			String userCd, 
			int loginAttempts, 
			String loginSuccess) throws 
			IntellipassException {
		
		logger.info ("Start insertUserRequest (" +
			"Connection, " +
			"String, " +
			"String, " +
			"int, " +
			"String");
		PreparedStatement ps = null;
		try {
			StringBuffer queryBuf = new StringBuffer ("INSERT INTO " +
					"user_request_detail (" +
					"SESSION_ID, " +
					"USER_CD, " +
					"LOGIN_ATMPTS_NBR, " +
					"LOGIN_SUCCESS, " +
					"USER_LOCKED, " +
					"CREATE_DATE, " +
					"CREATE_USER_CD) " +
					"VALUES (" +
					"?, ?, ?, ?, 'Y', CURRENT_TIMESTAMP, ?)");
			
			logger.debug ("Query: " + queryBuf.toString());
			ps = connection.prepareStatement(queryBuf.toString());
			queryBuf = null;
			
			ps.setString (1, sessionId);
			ps.setString (2, userCd);
			ps.setInt (3, loginAttempts);
			ps.setString (4, loginSuccess);
			ps.setString (5, "SYSTEM");
			
			ps.executeUpdate();
			logger.info ("End insertUserRequest (" +
					"Connection, " +
					"String, " +
					"String, " +
					"int, " +
					"String)");
		} catch (SQLException sqlexp) {
			logger.error(sqlexp.getMessage(), sqlexp);
			logger.info ("End insertUserRequest (" +
					"Connection, " +
					"String, " +
					"String, " +
					"int, " +
					"String)");
			throw new IntellipassException (sqlexp.getMessage());
		} finally {
			DbConnection.close(null, ps, null);
		}
	}

	/**
	 * 
	 * @param userCd
	 * @param loginAttempts
	 * @return
	 * @throws IntellipassException 
	 */
	private int processLockoutStatus (
			Connection connection, 
			String userCd,
			String sessionId, 
			int loginAttempts) throws 
			IntellipassException {
		
		int lockoutStatus = 0;
	
		ApplicationConfigVO applicationConfigVO = fetchApplicationConfig(connection);
	
		int minLoginAttempts = applicationConfigVO.getMinLoginAtmpt();		
		int maxLoginAttempts = applicationConfigVO.getMaxLoginAtmpt();
	
		int lockout = applicationConfigVO.getLoginAtmptLock();
		
		UserRequestVO[] userRequestVOs = fetchUserHistory(
				connection, 
				userCd);
		if (userRequestVOs.length > 10) {
			ArrayList <UserRequestVO> userRequestAL = new ArrayList <UserRequestVO> (Arrays.asList(userRequestVOs));
			UserRequestVO userRequestVO = new UserRequestVO ();
			userRequestVO.setSessionId(sessionId);
			
			userRequestAL.remove(userRequestVO);
						
			userRequestVOs = (UserRequestVO[]) userRequestAL.toArray(new UserRequestVO[userRequestAL.size()]);			
			userRequestAL = null;
			
			int successfulLoginNbrs = 0;
			for (int cnt = 0; cnt < userRequestVOs.length; cnt++) {
				if ("Y".equals (userRequestVOs[cnt].getLoginSuccess())) {
					successfulLoginNbrs++;
				}
			}
			if (successfulLoginNbrs/userRequestVOs.length >= .7) {
				minLoginAttempts = IntellipassUtil.getRandom(0, maxLoginAttempts - minLoginAttempts) + minLoginAttempts;
			} else {
				maxLoginAttempts = IntellipassUtil.getRandom(0, lockout - maxLoginAttempts) + maxLoginAttempts;
			}
		}
		if (loginAttempts >= minLoginAttempts && 
				loginAttempts <= maxLoginAttempts) {
			lockoutStatus = IntellipassUtil.getRandom(1, 2);			
		} else if (loginAttempts < minLoginAttempts) {
			lockoutStatus = -1;
		} 
		return lockoutStatus;
	}
	
	/**
	 * 
	 * @param connection
	 * @return
	 * @throws IntellipassException
	 */
	private ApplicationConfigVO fetchApplicationConfig (
			Connection connection) throws 
			IntellipassException {
		
		logger.info ("Start fetchApplicationConfig (Connection)");
		ApplicationConfigVO applicationConfigVO = new ApplicationConfigVO ();
		/*PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer queryBuf = new StringBuffer ("" +
					"SELECT " +
					"MIN_LOGIN_ATMPT, " +
					"MAX_LOGIN_ATMPT, " +
					"LOGIN_ATMPT_LOCK " +
					"FROM " +
					"application_config");
			logger.debug ("Query: " + queryBuf.toString());
			ps = connection.prepareStatement(queryBuf.toString());
			queryBuf = null;
			rs = ps.executeQuery();
			if (rs.next()) {
				int minLoginAtmpt = rs.getInt ("MIN_LOGIN_ATMPT");
				int maxLoginAtmpt = rs.getInt ("MAX_LOGIN_ATMPT");
				int loginAtmptLock = rs.getInt ("LOGIN_ATMPT_LOCK");
				
				applicationConfigVO.setMinLoginAtmpt(minLoginAtmpt);
				applicationConfigVO.setMaxLoginAtmpt(maxLoginAtmpt);
				applicationConfigVO.setLoginAtmptLock(loginAtmptLock);
			}
		} catch (SQLException sqlexp) {
			logger.error(sqlexp.getMessage(), sqlexp);
			throw new IntellipassException (sqlexp.getMessage());
		} finally {
			DbConnection.close(null, ps, rs);
		}*/
		ResourceBundle rb = ResourceBundle.getBundle("intellipass");
		String minLoginAtmpt = rb.getString("MIN_LOGIN_ATMPT");
		String maxLoginAtmpt = rb.getString("MAX_LOGIN_ATMPT");
		String loginAtmptLock = rb.getString("LOGIN_ATMPT_LOCK");
		
		int minLoginAttempt = Integer.parseInt(minLoginAtmpt);
		int maxLoginAttempt = Integer.parseInt(maxLoginAtmpt);
		int loginAttemptLock = Integer.parseInt(loginAtmptLock);
		
		applicationConfigVO.setMinLoginAtmpt(minLoginAttempt);
		applicationConfigVO.setMaxLoginAtmpt(maxLoginAttempt);
		applicationConfigVO.setLoginAtmptLock(loginAttemptLock);
		
		logger.info ("End fetchApplicationConfig (Connection)");
		return applicationConfigVO;
	}
	
	/**
	 * 
	 * @param connection
	 * @param userCd
	 * @return
	 */
	private UserRequestVO[] fetchUserHistory (
			Connection connection, 
			String userCd) throws IntellipassException {
		
		logger.info ("" +
				"Start fetchUserHistory (" +
				"Connection, " +
				"String)");
		UserRequestVO[] userRequestVOs = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer queryBuf = new StringBuffer ("" +
					"SELECT " +
					"SESSION_ID, " +
					"USER_CD, " +
					"LOGIN_ATMPTS_NBR, " +
					"LOGIN_SUCCESS, " +
					"USER_LOCKED " +
					"FROM " +
					"user_request_detail " +
					"WHERE " +
					"USER_CD = ?");
			logger.debug ("Query: " + queryBuf.toString());
			ps = connection.prepareStatement(queryBuf.toString());
			queryBuf = null;
			
			rs = ps.executeQuery();
			ArrayList<UserRequestVO> userRequestAL = new ArrayList<UserRequestVO> (10);
			while (rs.next()) {
				String sessionId = rs.getString("SESSION_ID");
				userCd = rs.getString("USER_CD");
				int loginAttempts = rs.getInt ("LOGIN_ATMPTS_NBR");
				String loginSuccess = rs.getString("LOGIN_SUCCESS");
				String userLocked = rs.getString("USER_LOCKED");
				
				UserRequestVO userRequestVO = new UserRequestVO ();
				userRequestVO.setSessionId(sessionId);
				userRequestVO.setUserCd(userCd);
				userRequestVO.setLoginAttempts(loginAttempts);
				userRequestVO.setLoginSuccess(loginSuccess);
				userRequestVO.setUserLocked(userLocked);
				
				userRequestAL.add(userRequestVO);
			}
			userRequestVOs = (UserRequestVO[]) userRequestAL.toArray(new UserRequestVO[userRequestAL.size()]);
			userRequestAL = null;
			logger.info ("" +
					"End fetchUserHistory (" +
					"Connection, " +
					"String)");
			return userRequestVOs;
		} catch (SQLException sqlexp) {
			logger.error(sqlexp.getMessage(), sqlexp);
			logger.info ("" +
					"End fetchUserHistory (" +
					"Connection, " +
					"String)");
			throw new IntellipassException (sqlexp.getMessage());
		} finally {
			DbConnection.close(null, ps, null);
		} 
		
	}
}

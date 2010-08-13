package com.appsec.intellipass.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.appsec.intellipass.exception.IntellipassException;

/**
 * 
 * @author Chander Singh
 * May 31, 2006
 */
public class DbConnection {

	/**
	 * 
	 * @return
	 * @throws IntellipassException
	 */
	public static Connection getConnection () throws 
		IntellipassException {
		try {
			Class.forName("com.mysql.jdbc.Driver");			
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intelli_pass", "intellipassuser", "7ui10I2ceU");
            return connection;
        } catch (SQLException sqlexp) {
            sqlexp.printStackTrace();
            throw new IntellipassException ("Failed to get Database Connection.");
        } catch (ClassNotFoundException cnfexp) {
            cnfexp.printStackTrace();
            throw new IntellipassException ("Database Operation failed.");
        }
	}

	/**
	 * 
	 * @param connection
	 * @param stmt
	 * @param rs
	 */
	public static void close (
			Connection connection, 
			Statement stmt, 
			ResultSet rs) {

		Logger logger = Logger.getLogger(DbConnection.class);
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException sqlexp) {
				logger.error (sqlexp.getMessage(), sqlexp);
			}
		}
		
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException sqlexp) {
				logger.error (sqlexp.getMessage(), sqlexp);
			}
		}
		
		if (connection != null ) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException sqlexp) {
				logger.error (sqlexp.getMessage(), sqlexp);
			}
		}
	}
}

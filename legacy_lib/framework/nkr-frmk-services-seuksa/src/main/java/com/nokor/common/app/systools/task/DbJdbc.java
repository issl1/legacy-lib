package com.nokor.common.app.systools.task;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * 
 * @author prasnar
 *
 */
public class DbJdbc implements Serializable {
	/** */
	private static final long serialVersionUID = -4412565433511626955L;

	private static final String DB_PROPS_FILE = "database.properties";

	private static final String SERVER = "db.serverName";
	private static final String PORT = "db.portNumber";
	private static final String DB_NAME = "db.database";
	private static final String USER = "db.userName";
	private static final String PWD = "db.password";

	private static String server;
	private static String port;
	private static String dbName;
	private static String user;
	private static String password;
	
	static {
		loadProperties(DB_PROPS_FILE);
	}
	
	/**
     * 
     * @param propFile
     */
    private static void loadProperties(String propFile) {
        try {
        	Properties props = null;
            InputStream fis = SysTaskHelper.class.getClassLoader().getResourceAsStream(propFile);
        	props = new Properties();
            props.load(fis);
            
            server = (String) props.get(SERVER);
            port = (String) props.get(PORT);
            dbName = (String) props.get(DB_NAME);
            user = (String) props.get(USER);
            password = (String) props.get(PWD);
            
            
            fis.close();
            
        } catch (Exception e) {
        	String errMsg = "Error while loading properties file [" + propFile + "]";
            throw new IllegalStateException(errMsg, e);
        }
    }

	/**
	 * @return the server
	 */
	public static String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public static void setServer(String server) {
		DbJdbc.server = server;
	}

	/**
	 * @return the port
	 */
	public static String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public static void setPort(String port) {
		DbJdbc.port = port;
	}

	/**
	 * @return the dbName
	 */
	public static String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public static void setDbName(String dbName) {
		DbJdbc.dbName = dbName;
	}

	/**
	 * @return the user
	 */
	public static String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public static void setUser(String user) {
		DbJdbc.user = user;
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public static void setPassword(String password) {
		DbJdbc.password = password;
	}

	/**
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:postgresql://" 
							+ server 
							+ ":" + port 
							+ "/" + dbName, user, password);
		} catch (Exception e) {
			String errMsg = "Error getConnection()";
            throw new IllegalStateException(errMsg, e);
		}
	}

    
}

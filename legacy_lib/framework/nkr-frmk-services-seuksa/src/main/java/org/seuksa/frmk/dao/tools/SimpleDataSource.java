/**
 * 
 */
package org.seuksa.frmk.dao.tools;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.seuksa.frmk.tools.exception.DaoException;

/**
 * Example of Datasource
 * Use a user JDBC Connection
 * @author prasnar
 * @version $Revision$
 */
public class SimpleDataSource implements DataSource {

	/** */
	private ConnectionVO connectionVo = null;

	/**
	 * Constructor
	 */
	public SimpleDataSource(String url, String driver, String user, String pwd) 
		throws DaoException {
		this (new ConnectionVO(driver, user, pwd, url));
	}

	/**
	 * Constructor
	 * @param conVO connnection vo
	 */
	public SimpleDataSource(ConnectionVO conVO) throws DaoException {
		try {
			Class.forName(conVO.getDriver());
		} catch (ClassNotFoundException e) {
			throw new DaoException("Ensure that the driver " + conVO.getDriver()
					+ "is your classpath.", e);
		}
		this.connectionVo = conVO;
	}

	/**
	 * @return Connection connection
	 */
	public Connection getConnection() throws SQLException {
		return getConnection(connectionVo.getUrl(), connectionVo.getUser(), connectionVo.getPwd());
	}

	public Connection getConnection(String arg0, String arg1)
			throws SQLException {
		throw new SQLException("getConnection(String arg0, String arg1) not implemented");
	}

	public PrintWriter getLogWriter() throws SQLException {
		throw new SQLException("getLogWriter() not implemented");
	}

	public int getLoginTimeout() throws SQLException {
		throw new SQLException("getLoginTimeout() not implemented");
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException {
		throw new SQLException("setLogWriter(PrintWriter arg0) not implemented");
	}

	public void setLoginTimeout(int arg0) throws SQLException {
		throw new SQLException("setLoginTimeout(int arg0) not implemented");
	}

	/**
	 * Get a JDBC Connection
	 * @return Connection
	 */
	protected Connection getConnection(String url, String user, String pwd) throws SQLException {
		return DriverManager.getConnection(url, user, pwd);
	}

	/**
	 * @return the connectionVo
	 */
	public ConnectionVO getConnectionVo() {
		return connectionVo;
	}

	/**
	 * @param connectionVo the connectionVo to set
	 */
	public void setConnectionVo(ConnectionVO connectionVo) {
		this.connectionVo = connectionVo;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}

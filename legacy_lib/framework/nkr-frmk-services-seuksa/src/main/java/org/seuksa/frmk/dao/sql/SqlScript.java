package org.seuksa.frmk.dao.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.systools.task.DbJdbc;


/**
 * @see org.apache.SqlScript.jdbc.ScriptRunner
 * 
 * @author prasnar
 *
 */
public class SqlScript {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static Logger LOGGER = LoggerFactory.getLogger(SqlScript.class);

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	private static final String DEFAULT_DELIMITER = ";";

	private Connection connection;

	private boolean stopOnError;
	private boolean autoCommit;
	private boolean sendFullScript;

	private String delimiter = DEFAULT_DELIMITER;
	private boolean fullLineDelimiter = false;

	/**
	 * 
	 * @param connection
	 */
	public SqlScript(Connection connection) {
		this.connection = connection;
	}
	

	/**
	 * 
	 * @param file
	 */
	public void runScriptFile(File file) {
		Reader reader = null;
		try {
			if (!file.exists() || file.isDirectory() || !file.getName().toLowerCase().endsWith(".sql")) {
				return;
			}
			
			logger.info("Run File [" + file.getAbsolutePath() + "]");
			reader = new BufferedReader(new FileReader(file));
			runScript(reader);
			
		} catch (Exception e) {
			logger.error("Failed to Execute" + file.getAbsolutePath() + " The error is " + e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (Exception e2) {}
		}
	}
		
	/**
	 * 
	 * @param reader
	 */
	public void runScript(Reader reader) {
//		setAutoCommit(false);

		try {
			if (sendFullScript) {
				executeFullScript(reader);
			} else {
				executeLineByLine(reader);
			}
		} finally {
//			rollbackConnection();
		}
	}

	/**
	 * 
	 * @param reader
	 */
	private void executeFullScript(Reader reader) {
		StringBuffer script = new StringBuffer();
		try {
			BufferedReader lineReader = new BufferedReader(reader);
			String line;
			while ((line = lineReader.readLine()) != null) {
				script.append(line);
				script.append(LINE_SEPARATOR);
			}
			execStatement(script.toString());
			commitConnection();
		} catch (Exception e) {
			String message = "Error executing: " + script + ".  Cause: " + e;
			logger.error(message);
			throw new DaoException(message, e);
		} finally {
			try {
				closeConnection();
			} catch (Exception e) { }; 
		}
	}

	/**
	 * 
	 * @param reader
	 */
	private void executeLineByLine(Reader reader) {
		StringBuffer command = new StringBuffer();
		try {
			BufferedReader lineReader = new BufferedReader(reader);
			String line;
			while ((line = lineReader.readLine()) != null) {
				command = handleLine(command, line);
			}
			commitConnection();
			checkForMissingLineTerminator(command);
		} catch (Exception e) {
			String message = "Error executing: " + command + ".  Cause: " + e;
			logger.error(message);
			throw new DaoException(message, e);
		} finally {
			try {
				closeConnection();
			} catch (Exception e) { }; 
		}
	}

	/**
	 * 
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * 
	 */
	private void setAutoCommit() {
		try {
			if (autoCommit != connection.getAutoCommit()) {
				connection.setAutoCommit(autoCommit);
			}
		} catch (Exception e) {
			throw new DaoException("Could not set AutoCommit to " + autoCommit + ". Cause: " + e.getMessage(), e);
		}
	}

	/**
	 * 
	 */
	public void commitConnection() {
		try {
			if (!connection.getAutoCommit()) {
				connection.commit();
			}
		} catch (Exception e) {
			throw new DaoException("Could not commit transaction. Cause: " + e.getMessage(), e);
		}
	}

	/**
	 * 
	 */
	private void rollbackConnection() {
		try {
			if (!connection.getAutoCommit()) {
				connection.rollback();
			}
		} catch (Throwable t) {
			// ignore
		}
	}

	/**
	 * 
	 * @param command
	 */
	private void checkForMissingLineTerminator(StringBuffer command) {
		if (command != null && command.toString().trim().length() > 0) {
			throw new DaoException("Line missing end-of-line terminator (" + delimiter + ") => " + command);
		}
	}

	/**
	 * 
	 * @param command
	 * @param line
	 * @return
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 */
	private StringBuffer handleLine(StringBuffer command, String line) throws SQLException, UnsupportedEncodingException {
		String trimmedLine = line.trim();
		if (lineIsComment(trimmedLine)) {
			logger.debug(trimmedLine);
		} else if (commandReadyToExecute(trimmedLine)) {
			command.append(line.substring(0, line.lastIndexOf(delimiter)));
			command.append(LINE_SEPARATOR);
			execStatement(command.toString());
			command.setLength(0);
		} else if (trimmedLine.length() > 0) {
			command.append(line);
			command.append(LINE_SEPARATOR);
		}
		return command;
	}

	/**
	 * 
	 * @param trimmedLine
	 * @return
	 */
	private boolean lineIsComment(String trimmedLine) {
		return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
	}

	/**
	 * 
	 * @param trimmedLine
	 * @return
	 */
	private boolean commandReadyToExecute(String trimmedLine) {
		return !fullLineDelimiter && trimmedLine.endsWith(delimiter)
				|| fullLineDelimiter && trimmedLine.equals(delimiter);
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	public static List<Object[]> execSql(String sql) {
		List<Object[]> res = null;
		SqlScript slqScript = null;
		try {
			Connection connection = DbJdbc.getConnection();
			slqScript = new SqlScript(connection);
			res = slqScript.execStatement(sql);
			slqScript.commitConnection();
		} catch (Exception e) {
			LOGGER.error("Failed to execute SQL s[" + (sql != null ? sql : "<NULL>") + "]", e);
		} finally {
			try {
				slqScript.closeConnection();
			} catch (Exception e) { }; 
		}
		return res;
	}

	/**
	 * 
	 * @param command
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 */
	private List<Object[]> execStatement(String command) {
		boolean hasResults = false;
		Statement statement = null;
		try {
			logger.debug(command);
			statement = connection.createStatement();
			if (stopOnError) {
				hasResults = statement.execute(command);
			} else {
				hasResults = statement.execute(command);
			}
			List<Object[]> results = hasResults ? getResults(statement) : new ArrayList<>();
			return results;
		} catch (Exception e) {
			logger.error("Failed to execute [" + command + "] ");
			logger.error(e.getMessage());
		}
		try {
			statement.close();
		} catch (Exception e) {
			// Ignore to workaround a bug in some connection pools
		}
		return null;
	}
	
	/**
	 * 
	 * @param statement
	 * @param hasResults
	 * @return
	 */
	private List<Object[]> getResults(Statement statement) {
		List<Object[]> results = new ArrayList<>();
		try {
			ResultSet rs = statement.getResultSet();
			if (rs != null) {
				ResultSetMetaData md = rs.getMetaData();
				int cols = md.getColumnCount();
//				for (int i = 0; i < cols; i++) {
//					String name = md.getColumnLabel(i + 1);
//					print(name + "\t");
//				}
				while (rs.next()) {
					Object[] row = new Object[cols];
					for (int i = 0; i < cols; i++) {
						String value = rs.getString(i + 1);
						row[i] = value;
					}
					results.add(row);
				}
			}
		} catch (SQLException e) {
			logger.error("Error printing results: " + e.getMessage());
		}
		return results;
	}

	/**
	 * 
	 * @param statement
	 * @param hasResults
	 */
	private void printResults(Statement statement, boolean hasResults) {
		try {
			if (hasResults) {
				ResultSet rs = statement.getResultSet();
				if (rs != null) {
					ResultSetMetaData md = rs.getMetaData();
					int cols = md.getColumnCount();
					for (int i = 0; i < cols; i++) {
						String name = md.getColumnLabel(i + 1);
						logger.debug(name + "\t");
					}
					logger.debug("");
					while (rs.next()) {
						for (int i = 0; i < cols; i++) {
							String value = rs.getString(i + 1);
							logger.debug(value + "\t");
						}
						logger.debug("");
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Error printing results: " + e.getMessage());
		}
	}


	/**
	 * 
	 * @param stopOnError
	 */
	public void setStopOnError(boolean stopOnError) {
		this.stopOnError = stopOnError;
	}

	/**
	 * 
	 * @param autoCommit
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	/**
	 * 
	 * @param sendFullScript
	 */
	public void setSendFullScript(boolean sendFullScript) {
		this.sendFullScript = sendFullScript;
	}


	/**
	 * 
	 * @param delimiter
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * 
	 * @param fullLineDelimiter
	 */
	public void setFullLineDelimiter(boolean fullLineDelimiter) {
		this.fullLineDelimiter = fullLineDelimiter;
	}

}

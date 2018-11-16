package com.nokor.efinance.tools.sync;

import java.io.Serializable;

/**
 * @author nora.ky
 *
 */
public class TaskManager implements Serializable {
	private static final long serialVersionUID = -5110376473572950778L;
	
	public void process() {
		// long processTime = System.currentTimeMillis();
		checkConnection();
	}
	
	/**
	 * 
	 */
	private void checkConnection() {
		ConnectionManager.getInstance().connectSyncServer();
	}
}

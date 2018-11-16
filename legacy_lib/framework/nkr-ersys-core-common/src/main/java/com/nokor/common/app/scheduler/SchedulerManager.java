package com.nokor.common.app.scheduler;

import java.util.List;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.scheduler.model.ScTriggerTask;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContext;
import com.nokor.frmk.security.context.SecApplicationContextHolder;


/**
 * 
 * @author phirun.kong
 *
 */

public class SchedulerManager implements AppServicesHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerManager.class);
	
	private final static String ATTR_SCHEDULE_MANAGER = "@schedulerManager@";
	
	private SchedulerFactory schedulerFactory;
	private SecApplicationContext secAppContext;
	
	/**
	 * 
	 */
	public SchedulerManager() {
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		try {
			logger.debug("Initialize SchedulerManager.");
			loadTasks();
		} catch (Exception e) {
			throw new Exception("Fail to init SchedulerManager: " + e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static SchedulerManager getSchedulerManager() throws Exception {
		return getSchedulerManager(SecApplicationContextHolder.getContext());
	}

	/**
	 * 
	 * @param secAppContext
	 * @return
	 * @throws Exception
	 */
	public static SchedulerManager getSchedulerManager(SecApplicationContext secAppContext) throws Exception {
		SchedulerManager res = (SchedulerManager) secAppContext.getAttribute(ATTR_SCHEDULE_MANAGER);
		if(res == null) {
			res = new SchedulerManager();
			res.secAppContext = secAppContext;
			secAppContext.putAttribute(ATTR_SCHEDULE_MANAGER, res);
		}
		return res;
	}
	
	
	/**
	 * Load schedulers from the database
	 */
	private void loadTasks() {
		logger.info("Loading tasks.");
		try {
			List<ScTriggerTask> triTasks = ENTITY_SRV.list(ScTriggerTask.class);
			for (ScTriggerTask triTask : triTasks) {
				try {
					logger.debug("Declare scheduler {" + triTask.getId() + ", " + triTask.getTask().getJobClassName() + "}.");
					Class<? extends Job> claJob = (Class<? extends Job>) Class.forName(triTask.getTask().getJobClassName());
					ScJobTrigger jobTrigger = new ScJobTrigger(getScheduler());
					jobTrigger.init(claJob, triTask.getId());
					jobTrigger.declare(triTask.getFrequency().getId().intValue(), triTask.getDay(), triTask.getHours(), triTask.getMinutes(), triTask.getExpression());
				} catch (ClassNotFoundException e) {
					logger.error("Class not found for task  {" + triTask.getTask().getId() + ", " + triTask.getTask().getJobClassName() + "}.", e);
				}				
			}			
		} catch (Exception e) {
			logger.error("Fail to load tasks.", e);
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void standBy() throws Exception {
		try {
			logger.debug("StandBy SchedulerManager().");
			getScheduler().standby();
		} catch (SchedulerException e) {
			throw new Exception("Fail to stabd-by scheduler: " + e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * @param schedulerName
	 * @throws Exception
	 */
	public void start(String schedulerName) throws Exception {
		try {
			logger.debug("Start SchedulerManager({" + schedulerName + "}).");
			getScheduler().start();
		} catch (SchedulerException e) {
			throw new Exception("Fail to start scheduler: " + e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void restart() throws Exception {
		stop();
		start();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		try {
			logger.debug("Start SchedulerManager().");
			getScheduler().start();
		} catch (SchedulerException e) {
			throw new Exception("Fail to start scheduler: " + e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		try {
			logger.debug("Stop SchedulerManager().");
			getScheduler().shutdown(true);
		} catch (SchedulerException e) {
			throw new Exception("Fail to start scheduler: " + e.getMessage(), e);
		}
	}
	
	
	/**
	 * Load the Quartz SCheduler
	 * @return
	 * @throws Exception
	 */
	private Scheduler getScheduler() throws Exception {
		try {
			if (schedulerFactory == null) {
				schedulerFactory = new StdSchedulerFactory();				
			}
			return schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			throw new Exception("Fail to get scheduler: " + e.getMessage(), e);
		}
	}
	
}

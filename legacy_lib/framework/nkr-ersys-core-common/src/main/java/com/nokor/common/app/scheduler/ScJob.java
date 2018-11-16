package com.nokor.common.app.scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author phirun.kong
 *
 */
public abstract class ScJob implements Job {
	
	protected static final String KEY_USERID = "MyJob#KEY_USERID";
	protected static final String KEY_SITEID = "MyJob#KEY_SITEID";
	protected static final String KEY_LOOPFRAME = "MyJob#KEY_LOOPFRAME";
	protected static final String KEY_LOOPINTERVAL = "MyJob#KEY_INTERVAL";
	protected static final String KEY_INTERRUPT = "MyJob#KEY_INTERRUPT";

	private JobExecutionContext jobExecutionContext = null;
	
	/**
	 * 
	 * @return
	 */
	private boolean loop() {
		int loopTimeFrame = this.getIntParameter(KEY_LOOPFRAME, 0);
		if (loopTimeFrame > 0) {
			int loopInterval = this.getIntParameter(KEY_LOOPINTERVAL, 0);
			try {
				if (this.interrupt()) {
					return false;
				}
				Thread.sleep(loopInterval);
				if (this.interrupt()) {
					return false;
				}
			} catch (InterruptedException e) {
				return false;
			}
			int currentFrame = (int) (new Date().getTime() - this.jobExecutionContext.getFireTime().getTime()) + loopInterval;
			return currentFrame < loopTimeFrame;
		} else {
			return false;
		}
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		this.jobExecutionContext = context;
		try {
			do {
				executeJob();
			} while (this.loop());
		} catch (Exception e) {
			throw new JobExecutionException(e, false);
		} finally {
		}
	}

	protected abstract void executeJob() throws Exception;

	
	/**
	 * 
	 * @return
	 */
	protected boolean interrupt() {
		if (this.getBooleanParameter(KEY_INTERRUPT, false)) {
			this.jobExecutionContext.getTrigger().getJobDataMap().put(KEY_INTERRUPT, false);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected int getIntParameter(String key, int def) {
		JobDataMap jdm = this.jobExecutionContext.getTrigger().getJobDataMap();
		if (jdm.containsKey(key)) {
			return jdm.getInt(key);
		} else {
			return def;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected boolean getBooleanParameter(String key, boolean def) {
		JobDataMap jdm = this.jobExecutionContext.getTrigger().getJobDataMap();
		if(jdm.containsKey(key)) {
			return jdm.getBoolean(key);
		} else {
			return def;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected String getStringParameter(String key, String def) {
		JobDataMap jdm = this.jobExecutionContext.getTrigger().getJobDataMap();
		if(jdm.containsKey(key)) {
			return jdm.getString(key);
		} else {
			return def;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected String getRequestParameterValue(String key, String def) {
		String[] res = this.getRequestParameterValues(key, null);
		if(res !=null && res.length > 0) {
			return res[0];
		} else {
			return def;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected String[] getRequestParameterValues(String key, String[] def) {
		Object obj = this.getObjectParameter(key, def);
		String[] res = def;
		if(obj instanceof String[]) {
			res = (String[]) obj;
		}
		return res;
	}
	
	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected Object getObjectParameter(String key, Object def) {
		JobDataMap jdm = this.jobExecutionContext.getTrigger().getJobDataMap();
		if(jdm.containsKey(key)) {
			return jdm.get(key);
		} else {
			return def;
		}
	}
	

}

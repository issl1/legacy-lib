package com.nokor.common.app.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author phirun.kong
 *
 */
public class ScJobTrigger {
	
	private static final Logger logger = LoggerFactory.getLogger(ScJobTrigger.class);
	
	private JobBuilder jobBuilder = null;
	private TriggerBuilder<Trigger> triggerBuilder = null;
	private Scheduler scheduler = null;
	
	/**
	 * 
	 * @param scheduler
	 * @param secAppContext
	 * @throws SchedulerException
	 */
	public ScJobTrigger(Scheduler scheduler) throws SchedulerException {
		this.scheduler = scheduler;
	}
	
	/**
	 * init
	 * @param claJob
	 * @param schedulerId
	 */
	public void init(Class<? extends Job> claJob, Long schedulerId) {
		logger.debug("Initialize Job builder and Trigger builder.");
		this.jobBuilder = newJob();
		this.triggerBuilder = newTrigger();
		jobBuilder.ofType(claJob).withIdentity(claJob.getName()).storeDurably();
		String key = claJob.getName() + "_" + schedulerId;
		triggerBuilder.withIdentity(key);
	}
	
	/**
	 * declare
	 * @param timeUnit
	 * @param day
	 * @param hours
	 * @param minutes
	 * @param expression
	 * @throws SchedulerException
	 */
	public void declare(Integer timeUnit, Integer day, Integer hours, Integer minutes, String expression) throws SchedulerException {
		JobDetail j = jobBuilder.build();
		if (!scheduler.checkExists(j.getKey())) {
			scheduler.addJob(j, true);
		} else {
			logger.debug("Job {" + j.getKey() + "} has been declared.");
		}
		ScheduleBuilder scheduleBuilder = getScheduleBuilder(timeUnit, day, hours, minutes, expression);
		triggerBuilder.withSchedule(scheduleBuilder);
		Trigger t = triggerBuilder.forJob(j).build();
		if (!scheduler.checkExists(t.getKey())) {
			scheduler.scheduleJob(t);
		} else {
			logger.debug("Trigger {" + t.getKey() + "} for Job {" + j.getKey() + "} has been declared.");
		}		
	}
	
	/**
	 * getScheduleBuilder
	 * @param timeUnit
	 * @param day
	 * @param hours
	 * @param minutes
	 * @param expression
	 * @return ScheduleBuilder
	 */
	public ScheduleBuilder getScheduleBuilder(Integer timeUnit, Integer day, Integer hours, Integer minutes, String expression) {
		ScheduleBuilder scheduleBuilder = null;
		String timeExpression = "";
		switch (timeUnit) {
			case 1:
				// In Minute
		  		scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
		  		((SimpleScheduleBuilder) scheduleBuilder).withIntervalInMinutes(minutes).repeatForever();
		  		break;
		  	case 2:
		  		// In Hour
		  		scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
		  		((SimpleScheduleBuilder) scheduleBuilder).withIntervalInHours(hours).repeatForever();
		  		break;
		  	case 3:
		  		// Daily
		  		scheduleBuilder = CronScheduleBuilder.dailyAtHourAndMinute(hours, minutes).withMisfireHandlingInstructionFireAndProceed();
		  		break;
		  	case 4:
		  		// Weekly
		  		scheduleBuilder = CronScheduleBuilder.weeklyOnDayAndHourAndMinute(day, hours, minutes).withMisfireHandlingInstructionFireAndProceed();
		  		break;
		  	case 5:
		  		// Monthly
		  		scheduleBuilder = CronScheduleBuilder.monthlyOnDayAndHourAndMinute(day, hours, minutes).withMisfireHandlingInstructionFireAndProceed();
		  		break;
			case 6:
				// First Day of Month
				timeExpression = getTimeExpression(hours, minutes);
				// Example: "0 30 6 1 * ?"
				String beginOfMonthExpression = "0 " + timeExpression + " 1 * ?";
				scheduleBuilder = CronScheduleBuilder.cronSchedule(beginOfMonthExpression);
		  		break;
			case 7:
				// Last Day of Month
				timeExpression = getTimeExpression(hours, minutes);
				// Example: "0 30 6 L * ?"
				String endOfMonthExpression = "0 " + timeExpression + " L * ?";
				scheduleBuilder = CronScheduleBuilder.cronSchedule(endOfMonthExpression);
		  		break;
			case 8:
				// Expression
				// Example: "0 30 6 1 * ?"
				scheduleBuilder = CronScheduleBuilder.cronSchedule(expression);
		  		break;
		}
		
		return scheduleBuilder;
	}
	
	/**
	 * getTimeExpression
	 * @param hours
	 * @param minutes
	 * @return String
	 */
	public String getTimeExpression(Integer hours, Integer minutes) {
		String strHours = "0";
		String strMinutes = "0";
		if (hours != null) {
			strHours = hours.toString();
		}
		if (minutes != null) {
			strMinutes = minutes.toString();
		}
		return  strMinutes + " " + strHours;
	}
	
	/**
	 * 
	 * @return
	 */
	public TriggerBuilder<Trigger> getTriggerBuilder() {
		return triggerBuilder;
	}
	
}

package com.nokor.common.app.scheduler.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.MainEntity;

/**
 * 
 * @author kong.phirun
 *
 */
@Entity
@Table(name = "tu_sc_trigger_task")
public class ScTriggerTask extends MainEntity {
	/** */
	private static final long serialVersionUID = 5729057969821479125L;

	private String desc;
	private ScTask task;
	private ScFrequency frequency;
	private Integer day;
	private Integer hours;
	private Integer minutes;
	private String expression;
	private String comment;
	
	 /**
     * 
     * @return
     */
    public static ScTriggerTask createInstance() {
        ScTriggerTask tas = EntityFactory.createInstance(ScTriggerTask.class);
        return tas;
    }
    
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tri_tas_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
	 * @return the desc
	 */
    @Column(name = "tri_tas_desc", nullable = false)
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the task
	 */
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "tri_tas_tas_id")
	public ScTask getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(ScTask task) {
		this.task = task;
	}	

	
	/**
	 * @return the frequency
	 */
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "sch_fre_id")
	public ScFrequency getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(ScFrequency frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the day
	 */
	@Column(name = "tri_tas_day")
	public Integer getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(Integer day) {
		this.day = day;
	}

	/**
	 * @return the hours
	 */
	@Column(name = "tri_tas_hours")
	public Integer getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(Integer hours) {
		this.hours = hours;
	}

	/**
	 * @return the minutes
	 */
	@Column(name = "tri_tas_minutes")
	public Integer getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return the expression
	 */
	@Column(name = "tri_tas_expression", length = 50)
	public String getExpression() {
		return expression;
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	/**
	 * @return the comment
	 */
	@Column(name = "tri_tas_comment", length = 255)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}

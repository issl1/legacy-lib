package com.nokor.ersys.collab.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_task_recipient")
public class TaskRecipient extends EntityWkf implements MTaskRecipient {
	/** */
	private static final long serialVersionUID = -5165665582173496131L;
	
	private Task task;
	private Employee sender;
	private Employee recipient;
	private ERecipientType typeRecipient;
	private Date when;

	/**
	 * 
	 * @return
	 */
	public static TaskRecipient createInstance() {
		TaskRecipient empTask = EntityFactory.createInstance(TaskRecipient.class);
        return empTask;
    }

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tas_rec_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the task
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tas_id", nullable = false)
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * @return the sender
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sender_emp_id", nullable = false)
	public Employee getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(Employee sender) {
		this.sender = sender;
	}

	/**
	 * @return the recipient
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="recipient_emp_id", nullable = false)
	public Employee getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(Employee recipient) {
		this.recipient = recipient;
	}

	/**
	 * @return the typeRecipient
	 */
    @Column(name = "rec_typ_id", nullable = false)
    @Convert(converter = ERecipientType.class)
	public ERecipientType getTypeRecipient() {
		return typeRecipient;
	}

	/**
	 * @param typeRecipient the typeRecipient to set
	 */
	public void setTypeRecipient(ERecipientType typeRecipient) {
		this.typeRecipient = typeRecipient;
	}

	/**
	 * @return the when
	 */
    @Column(name = "tas_rec_when", nullable = false)
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when the when to set
	 */
	public void setWhen(Date when) {
		this.when = when;
	}

	
}

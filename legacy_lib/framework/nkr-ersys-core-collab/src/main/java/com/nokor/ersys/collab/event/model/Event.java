package com.nokor.ersys.collab.event.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_event")
public class Event extends EntityA {
	/** */
	private static final long serialVersionUID = -2064240742896375484L;

	private String name;
	private String desc;
	private Date startDate;
	private Date endDate;
	private ETypeEvent type;
	private EStatusEvent status;
	private String comment;


	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eve_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	

	/**
	 * @return the name
	 */
	@Column(name = "eve_name", nullable = false)
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * @return the desc
	 */
	@Column(name = "eve_desc", nullable = true)
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
	 * @return the startDate
	 */
	@Column(name = "eve_start_date", nullable = false)
	public Date getStartDate() {
		return startDate;
	}



	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	/**
	 * @return the endDate
	 */
	@Column(name = "eve_end_date", nullable = false)
	public Date getEndDate() {
		return endDate;
	}



	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	/**
	 * @return the type
	 */
    @Column(name="typ_eve_id", nullable = false)
    @Convert(converter = ETypeEvent.class)
	public ETypeEvent getType() {
		return type;
	}



	/**
	 * @param type the type to set
	 */
	public void setType(ETypeEvent type) {
		this.type = type;
	}



	/**
	 * @return the status
	 */
    @Column(name="sta_eve_id", nullable = false)
    @Convert(converter = EStatusEvent.class)
	public EStatusEvent getStatus() {
		return status;
	}



	/**
	 * @param status the status to set
	 */
	public void setStatus(EStatusEvent status) {
		this.status = status;
	}



	/**
	 * @return the comment
	 */
	@Column(name = "eve_comment", nullable = true)
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

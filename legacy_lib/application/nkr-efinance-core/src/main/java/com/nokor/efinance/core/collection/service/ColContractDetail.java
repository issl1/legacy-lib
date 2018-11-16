package com.nokor.efinance.core.collection.service;

import java.io.Serializable;
import java.util.Date;

import com.nokor.efinance.core.address.model.Area;

public class ColContractDetail implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -7946908990949162112L;
	
	private Long id;	
	private Integer nbOverdueInDays;
	private Integer debtLevel;
	private Integer numberGuarantors;
	private Integer dueDay;
	private Date firstDueDate;
	private Long appId;
	private Long indId;
	private Long comId;
	private Long teamId;
	private Long staffId;
	private Area area;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the nbOverdueInDays
	 */
	public Integer getNbOverdueInDays() {
		return nbOverdueInDays;
	}
	/**
	 * @param nbOverdueInDays the nbOverdueInDays to set
	 */
	public void setNbOverdueInDays(Integer nbOverdueInDays) {
		this.nbOverdueInDays = nbOverdueInDays;
	}
	
	/**
	 * @return the dueDay
	 */
	public Integer getDueDay() {
		return dueDay;
	}
	/**
	 * @param dueDay the dueDay to set
	 */
	public void setDueDay(Integer dueDay) {
		this.dueDay = dueDay;
	}
	/**
	 * @return the debtLevel
	 */
	public Integer getDebtLevel() {
		return debtLevel;
	}
	/**
	 * @param debtLevel the debtLevel to set
	 */
	public void setDebtLevel(Integer debtLevel) {
		this.debtLevel = debtLevel;
	}		
	/**
	 * @return the firstDueDate
	 */
	public Date getFirstDueDate() {
		return firstDueDate;
	}
	/**
	 * @param firstDueDate the firstDueDate to set
	 */
	public void setFirstDueDate(Date firstDueDate) {
		this.firstDueDate = firstDueDate;
	}
	/**
	 * @return the numberGuarantors
	 */
	public Integer getNumberGuarantors() {
		return numberGuarantors;
	}
	/**
	 * @param numberGuarantors the numberGuarantors to set
	 */
	public void setNumberGuarantors(Integer numberGuarantors) {
		this.numberGuarantors = numberGuarantors;
	}	
	/**
	 * @return the appId
	 */
	public Long getAppId() {
		return appId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	/**
	 * @return the teamId
	 */
	public Long getTeamId() {
		return teamId;
	}
	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	/**
	 * @return the staffId
	 */
	public Long getStaffId() {
		return staffId;
	}
	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	/**
	 * @return the area
	 */
	public Area getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}
	/**
	 * @return the indId
	 */
	public Long getIndId() {
		return indId;
	}
	/**
	 * @param indId the indId to set
	 */
	public void setIndId(Long indId) {
		this.indId = indId;
	}
	/**
	 * @return the comId
	 */
	public Long getComId() {
		return comId;
	}
	/**
	 * @param comId the comId to set
	 */
	public void setComId(Long comId) {
		this.comId = comId;
	}
}

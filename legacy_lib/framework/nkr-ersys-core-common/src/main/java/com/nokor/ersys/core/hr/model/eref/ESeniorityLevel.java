package com.nokor.ersys.core.hr.model.eref;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.eref.BaseERefEntity;


/**
 * Seniority Level
 * 
 * @author ly.youhort
 */
@Entity
@Table(name="tu_senority_level")
public class ESeniorityLevel extends BaseERefEntity implements AttributeConverter<ESeniorityLevel, Long> {
	/** */
	private static final long serialVersionUID = 3975460714479466937L;

	private EEmploymentStatus employmentStatus;

	/**
	 * 
	 */
	public ESeniorityLevel() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESeniorityLevel(String code, long id) {
		super(code, id);
	}

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sen_lev_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the employmentStatus
	 */
    @Column(name = "emp_sta_id", nullable = false)
    @Convert(converter = EEmploymentStatus.class)
	public EEmploymentStatus getEmploymentStatus() {
		return employmentStatus;
	}

	/**
	 * @param employmentStatus the employmentStatus to set
	 */
	public void setEmploymentStatus(EEmploymentStatus employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	@Override
	public ESeniorityLevel convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ESeniorityLevel arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ESeniorityLevel> values() {
		return getValues(ESeniorityLevel.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ESeniorityLevel getByCode(String code) {
		return getByCode(ESeniorityLevel.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESeniorityLevel getById(long id) {
		return getById(ESeniorityLevel.class, id);
	}
	
	/**
	 * 
	 * @param status
	 * @return
	 */
	public static List<ESeniorityLevel> getByField(EEmploymentStatus status) {
		List<ESeniorityLevel> lst = new ArrayList<>();
		for (ESeniorityLevel val : values()) {
			if (val.getEmploymentStatus().equals(status)) {
				lst.add(val);
			}
		}
		return lst;
	}
}

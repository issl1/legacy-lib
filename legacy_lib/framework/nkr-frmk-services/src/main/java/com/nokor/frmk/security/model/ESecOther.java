package com.nokor.frmk.security.model;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.eref.BaseERefEntity;

/**
 * Created on 08/09/2015
 * ESecOther
 * @author phirun.kong
 *
 */
@Entity
@Table(name = "ts_sec_other")
public class ESecOther extends BaseERefEntity implements MESecOther, AttributeConverter<ESecOther, Long> {

	/**	 */
	private static final long serialVersionUID = 4758915059944193432L;
	
	private SecApplication application;
	private SecControl control;
	
	/**
	 * 
	 */
	public ESecOther() {
	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESecOther(long id) {
		super(null, id);
	}
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_oth_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
	/**
	 * @return the application
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sec_app_id", nullable = false)
	public SecApplication getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(SecApplication application) {
		this.application = application;
	}

	@Override
	public ESecOther convertToEntityAttribute(Long id) {
		return ((ESecOther) super.convertToEntityAttribute(id));
	}
	
	@Override
	public Long convertToDatabaseColumn(ESecOther arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ESecOther> values() {
		return getValues(ESecOther.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESecOther getById(long id) {
		return getById(ESecOther.class, id);
	}
	
	/**
	 * @return the control
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sec_ctl_id", nullable = true)
	public SecControl getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(SecControl control) {
		this.control = control;
	}
	
}

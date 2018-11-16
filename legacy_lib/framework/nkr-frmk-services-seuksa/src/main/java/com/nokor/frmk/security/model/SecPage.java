package com.nokor.frmk.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_sec_page")
public class SecPage extends EntityRefA implements MSecPage {
	/** */
	private static final long serialVersionUID = -3898636357368435348L;

	private SecApplication application;
	private SecControl control;
	
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_pag_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_pag_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_pag_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "sec_pag_desc_en", nullable = true)
	@Override
    public String getDescEn() {
        return descEn;
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

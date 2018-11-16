package com.nokor.common.app.eventlog.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "ts_sec_event_action")
public class SecEventAction extends EntityRefA {
	/** */
	private static final long serialVersionUID = -4879656684786051146L;
	
	public static final long LOGIN = 1L; 
	public static final long LOGOUT = 2L; 

	private ESecEventType type;
	private ESecEventLevel level;

	/**
      * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
      */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_eve_act_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
     */
    @Column(name = "sec_eve_act_code", nullable = false)
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "sec_eve_act_desc", nullable = false)
    @Override
    public String getDesc() {
        return desc;
    }
    
    /**
	 * @return the type
	 */
    @Column(name = "sec_eve_typ_id", nullable = false)
    @Convert(converter = ESecEventType.class)
	public ESecEventType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ESecEventType type) {
		this.type = type;
	}

	/**
	 * @return the level
	 */
    @Column(name = "sec_eve_lev_id", nullable = false)
    @Convert(converter = ESecEventLevel.class)
	public ESecEventLevel getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(ESecEventLevel level) {
		this.level = level;
	}

    @Transient
    public boolean isLogin() {
    	return LOGIN == getId();
    }
    
    @Transient
    public boolean isLogout() {
    	return LOGOUT == getId();
    }
}

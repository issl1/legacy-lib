package com.nokor.frmk.config.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;



/**
 * 
 * @author prasnar
 */
@Entity
@Table(name = "ts_setting_config")
public class SettingConfig extends EntityRefA {
 	/** */
	private static final long serialVersionUID = 3664467955665016945L;
	
	private Long secAppId;
	private String topic;
	private String value;
	private String comment;
    private Boolean readOnly;
    private Boolean visible;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_cfg_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "set_cfg_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}

	/**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "set_cfg_desc", nullable = false)
   	@Override
    public String getDesc() {
        return desc;
    }
    
	/**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
     */
    @Column(name = "set_cfg_desc_en", nullable = true)
   	@Override
    public String getDescEn() {
        return descEn;
    }
    
    /**
	 * @return the secAppId
	 */
    @Column(name = "sec_app_id", nullable = true)
    	public Long getSecAppId() {
		return secAppId;
	}

	/**
	 * @param secAppId the secAppId to set
	 */
	public void setSecAppId(Long secAppId) {
		this.secAppId = secAppId;
	}

	/**
     * @return readOnly
     */
	@Column(name = "set_cfg_read_only")
    public Boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly
     */
    public void setReadOnly(final Boolean readOnly) {
        this.readOnly = readOnly;
    }

	/**
	 * @return the visible
	 */
	@Column(name = "set_cfg_visible")
	public Boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * @return the topic
	 */
    @Column(name = "set_cfg_topic", nullable = true)
	public String getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the value
	 */
    @Column(name = "set_cfg_value", nullable = false)
	public String getValue() {
        return value;
    }

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	

	/**
	 * @return the comment
	 */
	@Column(name = "set_cfg_comment", nullable = true)
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

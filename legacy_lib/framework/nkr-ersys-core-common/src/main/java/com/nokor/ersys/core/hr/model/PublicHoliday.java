package com.nokor.ersys.core.hr.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.ersys.core.hr.model.organization.MPublicHoliday;

/**
 * Public Holiday days
 * 
 * @author ly.youhort
 */
@Entity
@Table(name="tu_public_holiday")
public class PublicHoliday extends EntityRefA implements MPublicHoliday {
	/** */
	private static final long serialVersionUID = 4731314525353746288L;

	private Date day;
	private Boolean isDayOff;

	/**
	 * 
	 */
	public PublicHoliday() {
	}
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pub_hol_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
     * create PublicHoliday's Instance
     * @return
     */
    public static PublicHoliday createInstance() {
    	PublicHoliday holiday = EntityFactory.createInstance(PublicHoliday.class);
		return holiday;
	}
    

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "pub_hol_code", nullable = true, length = 10)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "pub_hol_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "pub_hol_desc_en", nullable = false, length = 100)
	@Override
	public String getDescEn() {
		return descEn;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "pub_hol_day", nullable = false)
	public Date getDay() {
		return day;
	}

	
	/**
	 * 
	 * @param day
	 */
	public void setDay(Date day) {
		this.day = day;
	}

	/**
	 * @return the isDayOff
	 */
    @Column(name = "pub_hol_is_day_off", nullable = false)
	public Boolean isDayOff() {
		return Boolean.TRUE.equals(isDayOff);
	}

	/**
	 * @param isDayOff the isDayOff to set
	 */
	public void setDayOff(Boolean isDayOff) {
		this.isDayOff = isDayOff;
	}


}
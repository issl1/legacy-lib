package com.nokor.ersys.collab.event.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.frmk.security.model.SecUser;

@Entity
@Table(name = "tu_calendar")
public class Calendar extends EntityRefA {
	/** */
	private static final long serialVersionUID = 4172003899729306430L;

	private SecUser owner;
	private String comment;


	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cal_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "cal_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "cal_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "cal_desc_en", nullable = false, length = 100)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the owner
	 */
    @ManyToOne
    @JoinColumn(name="owner_usr_sec_id", nullable = false)
	public SecUser getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(SecUser owner) {
		this.owner = owner;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "cal_comment", nullable = true)
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

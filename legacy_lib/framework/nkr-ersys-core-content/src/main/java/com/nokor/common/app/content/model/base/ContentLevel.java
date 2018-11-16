package com.nokor.common.app.content.model.base;

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
 *
 */
@Entity
@Table(name="ts_cms_content_level")
public class ContentLevel extends EntityRefA {
	/** */
	private static final long serialVersionUID = -1712072534320406098L;

	public static final long LEV1 = 1L; // Partie I, II...
	public static final long LEV2 = 2L; // Titre I, II...
	public static final long LEV3 = 3L; // Chapitre I, II...
	public static final long LEV4 = 4L; // Section 1, 2...
	public static final long LEV5 = 5L; // I. II....
	public static final long LEV6 = 6L; // A., B....
	public static final long LEV7 = 7L; // 1., 2....
	public static final long LEV8 = 8L; // a., b....
	public static final long LEV9 = 9L; // i., ii....
	public static final long LEV10 = 10L; // 
	public static final long LEV11 = 11L; // 
	public static final long LEV12 = 12L; // 
	public static final long LEV13 = 13L; // 
	public static final long LEV14 = 14L; // 
	public static final long LEV15 = 15L; // 
	public static final long LEV16 = 16L; // 
	public static final long LEV17 = 17L; // 
	public static final long LEV18 = 18L; // 
	public static final long LEV19 = 19L; // 
	public static Long LEV20 = 20L; // 1, 2...

	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cnt_lev_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "cnt_lev_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "cnt_lev_desc", nullable = false, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }


}

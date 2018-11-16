package com.nokor.common.app.cfield.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Customize fields
 * Table 
 * @author prasnar
 */
@Entity
@Table(name = "ts_cus_table")
public class CusTable extends EntityRefA {
	/** */
	private static final long serialVersionUID = -3315525724396067877L;


	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cus_tab_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "cus_tab_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "cus_tab_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

    
   
}

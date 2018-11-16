package com.nokor.frmk.config.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Topic Reference model
 * @author kong.phirun
 */
@Entity
@Table(name = "ts_ref_topic")
public class RefTopic extends EntityRefA implements MRefTable {

	private List<RefTable> tables;
	private RefTopic parent;


    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref_top_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "ref_top_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}
    
	/**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "ref_top_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

    @Column(name = "ref_top_desc_en", nullable = true)
   	@Override	
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the tables
	 */
    @ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="ts_ref_topic_table",
				joinColumns = { @JoinColumn(name = "ref_top_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "ref_tab_id") })
	public List<RefTable> getTables() {
		return tables;
	}

	/**
	 * @param tables the tables to set
	 */
	public void setTables(List<RefTable> tables) {
		this.tables = tables;
	}

	/**
	 * @return the parent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_ref_top_id", nullable = true)
	public RefTopic getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(RefTopic parent) {
		this.parent = parent;
	}

}

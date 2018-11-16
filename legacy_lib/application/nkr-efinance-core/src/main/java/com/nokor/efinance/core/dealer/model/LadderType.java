package com.nokor.efinance.core.dealer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_ladder_type")
public class LadderType extends EntityRefA {
	
 	private static final long serialVersionUID = -3400594583337287752L;

	/**
     * @return
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lad_typ_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Transient
    public String getCode() {
        return "TMP";
    }

    /**
     * 
     * @return <String>
     */
    @Override
    @Column(name = "lad_typ_desc", nullable = true, length = 255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * @return <String>
     */
    @Override
    @Column(name = "lad_typ_desc_en", nullable = false, length = 255)
    public String getDescEn() {
        return super.getDescEn();
    }
}

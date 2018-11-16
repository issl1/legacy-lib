package com.nokor.efinance.core.dealer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_dealer_group")
public class DealerGroup extends EntityRefA {
	
    /**
	 */
	private static final long serialVersionUID = -1249349064549155052L;
	
	private LadderType ladderType;

	/**
     * @return
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_grp_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Transient
    public String getCode() {
        return "TMP";
    }

    /**
     * @return <String>
     */
    @Override
    @Column(name = "dea_grp_desc", nullable = true, length = 255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * @return <String>
     */
    @Override
    @Column(name = "dea_grp_desc_en", nullable = false, length = 255)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the ladderType
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lad_typ_id", nullable = true)
	public LadderType getLadderType() {
		return ladderType;
	}

	/**
	 * @param ladderType the ladderType to set
	 */
	public void setLadderType(LadderType ladderType) {
		this.ladderType = ladderType;
	}    
}

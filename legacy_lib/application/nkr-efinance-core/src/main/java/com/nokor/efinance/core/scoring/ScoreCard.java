package com.nokor.efinance.core.scoring;

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
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_score_card")
public class ScoreCard extends EntityRefA {

	private static final long serialVersionUID = -2109001780570872581L;

	private ScoreGroup scoreGroup;
	
	private int type;
	private String typeValue;
	private double scoreValue;
	
    /**
     * @return
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sco_cad_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "sco_cad_code", nullable = false, length = 20, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the asset range's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "sco_cad_desc", nullable = true, length = 255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * 
     * @return <String>
     */
    @Override
    @Column(name = "sco_cad_desc_en", nullable = false, length = 255)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the scoreGroup
	 */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sco_grp_id", nullable = true)
	public ScoreGroup getScoreGroup() {
		return scoreGroup;
	}

	/**
	 * @param scoreGroup the scoreGroup to set
	 */
	public void setScoreGroup(ScoreGroup scoreGroup) {
		this.scoreGroup = scoreGroup;
	}

	/**
	 * @return the type
	 */
	@Column(name = "sco_cad_type", nullable = true)
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the typeValue
	 */
	@Column(name = "sco_cad_type_value", nullable = true)
	public String getTypeValue() {
		return typeValue;
	}

	/**
	 * @param typeValue the typeValue to set
	 */
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

	/**
	 * @return the scoreValue
	 */
	@Column(name = "sco_cad_score_value", nullable = true)
	public double getScoreValue() {
		return scoreValue;
	}

	/**
	 * @param scoreValue the scoreValue to set
	 */
	public void setScoreValue(double scoreValue) {
		this.scoreValue = scoreValue;
	}		
}

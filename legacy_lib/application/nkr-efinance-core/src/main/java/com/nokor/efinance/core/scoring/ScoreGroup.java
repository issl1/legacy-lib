package com.nokor.efinance.core.scoring;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Asset Range Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_score_group")
public class ScoreGroup extends EntityRefA {

	private static final long serialVersionUID = -2109001780570872581L;

	private List<ScoreCard> scoreCard; 
	
    /**
     * @return
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sco_grp_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "sco_grp_code", nullable = false, length = 20, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * 
     * @return <String>
     */
    @Override
    @Column(name = "sco_grp_desc", nullable = true, length = 255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset range's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "sco_grp_desc_en", nullable = false, length = 255)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the scoreCard
	 */
    @OneToMany(mappedBy="scoreGroup", fetch = FetchType.LAZY)
	public List<ScoreCard> getScoreCard() {
		return scoreCard;
	}

	/**
	 * @param scoreCard the scoreCard to set
	 */
	public void setScoreCard(List<ScoreCard> scoreCard) {
		this.scoreCard = scoreCard;
	}
    
    
}

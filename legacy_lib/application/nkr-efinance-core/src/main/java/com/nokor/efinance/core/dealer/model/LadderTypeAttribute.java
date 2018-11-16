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

import org.seuksa.frmk.model.entity.EntityA;
/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tu_ladder_type_attribute")
public class LadderTypeAttribute extends EntityA {

	/** */
	private static final long serialVersionUID = 8254662642847208730L;
	
	private LadderType ladderType;
	private Double from;
	private Double to;
	private Double amount;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lad_typ_att_id", unique = true, nullable = false)
	public Long getId() {
		return id;
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

	/**
	 * @return the from
	 */
	@Column(name = "lad_typ_att_from", nullable = true, length = 255)
	public Double getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Double from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	@Column(name = "lad_typ_att_to", nullable = true, length = 255)
	public Double getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Double to) {
		this.to = to;
	}

	/**
	 * @return the amount
	 */
	@Column(name = "lad_typ_att_amount", nullable = true, length = 255)
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	

}

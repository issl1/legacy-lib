package com.nokor.common.app.content.model.event;

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

import com.nokor.common.app.content.model.base.Classification;

/**
 * An event can have several classifications
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_event_classification")
public class EventClassification extends EntityA {
	/** */
	private static final long serialVersionUID = 8683013568240080656L;

	private Event event;
	private Classification classification;
    private Integer sortIndex;

	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_cla_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the event
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eve_id", nullable = false)
	public Event getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
	}

	/**
	 * @return the classification
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cla_id", nullable = false)
	public Classification getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	/**
	 * @return the sortIndex
	 */
	@Column(name = "sort_index", nullable = false)
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	    
}

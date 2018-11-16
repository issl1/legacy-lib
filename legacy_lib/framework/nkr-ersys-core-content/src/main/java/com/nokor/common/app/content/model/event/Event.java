package com.nokor.common.app.content.model.event;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.content.model.base.AbstractContent;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_cms_event")
public class Event extends AbstractContent {
	/** */
	private static final long serialVersionUID = 7575950404333434458L;

    private TypeEvent typeEvent;
	private List<EventClassification> classifications;
	private List<EventAttachment> attachments;
    private String place;
    private Integer numSeats;
    private String comment;

	private List<EventRegistrant> registrants;
    
    
    /**
     * 
     */
    public static Event createInstance() {
    	Event eve = EntityFactory.createInstance(Event.class);
    	
    	return eve;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eve_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
	 * @return the classifications
	 */
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	public List<EventClassification> getClassifications() {
		return classifications;
	}

	/**
	 * @param classifications the classifications to set
	 */
	public void setClassifications(List<EventClassification> classifications) {
		this.classifications = classifications;
	}

	/**
	 * @return the attachments
	 */
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	public List<EventAttachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<EventAttachment> attachments) {
		this.attachments = attachments;
	}
	
    @Column(name = "eve_place")
    public String getPlace() {
        return place;
    }

    public void setPlace(final String place) {
        this.place = place;
    }

    @Column(name = "eve_num_seats")
    public Integer getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(final Integer numSeats) {
        this.numSeats = numSeats;
    }


	/**
	 * @return the registrants
	 */
	@OneToMany(mappedBy = "event")
	public List<EventRegistrant> getRegistrants() {
		return registrants;
	}

	/**
	 * @param registrants the registrants to set
	 */
	public void setRegistrants(List<EventRegistrant> registrants) {
		this.registrants = registrants;
	}

	@Column(name = "eve_comment")
    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    @ManyToOne()
    @JoinColumn(name = "typ_eve_id", nullable = false)
    public TypeEvent getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(final TypeEvent typeEvent) {
        this.typeEvent = typeEvent;
    }

	
}

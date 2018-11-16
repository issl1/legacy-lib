package com.nokor.common.app.content.model.event;

import java.util.Date;

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

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.hr.model.organization.Employee;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_cms_event_registrant")
public class EventRegistrant extends EntityWkf {
	/** */
	private static final long serialVersionUID = 4421743062801415432L;

	private Event event;
	private Date registrationDate;
	private Employee registrant;
	
    
    /**
     * 
     */
    public static EventRegistrant createInstance() {
    	EventRegistrant eveReg = EntityFactory.createInstance(EventRegistrant.class);
    	return eveReg;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eve_reg_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the event
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eve_id", nullable = false)
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
	 * @return the registrationDate
	 */
    @Column(name = "eve_reg_dt_registration", nullable = false)
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the registrant
	 */
	@Transient
	public Employee getRegistrant() {
		return registrant;
	}

	/**
	 * @param registrant the registrant to set
	 */
	public void setRegistrant(Employee registrant) {
		this.registrant = registrant;
	}
	
    
    
}

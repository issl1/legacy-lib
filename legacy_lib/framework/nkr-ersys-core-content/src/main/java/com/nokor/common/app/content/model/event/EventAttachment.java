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

import com.nokor.common.app.content.model.file.FileData;


/**
 * An event can have several attachments
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_event_attachment")
public class EventAttachment extends EntityA {
	/** */
	private static final long serialVersionUID = 4588863212948089810L;

	private Event event;
	private FileData attachment;
    private Integer sortIndex;

	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_att_id", unique = true, nullable = false)
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
	 * @return the attachment
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="att_id", nullable = false)
	public FileData getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(FileData attachment) {
		this.attachment = attachment;
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

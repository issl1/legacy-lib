package com.nokor.ersys.collab.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.content.model.file.FileData;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_task_attachment")
public class TaskAttachment extends EntityA implements MTaskAttachment {
	/** */
	private static final long serialVersionUID = 7482825340613304418L;

	private String comment;
	private FileData attachment;
	private String desc;

	/**
	 * 
	 * @return
	 */
	public static TaskAttachment createInstance() {
		TaskAttachment comm = EntityFactory.createInstance(TaskAttachment.class);
        return comm;
    }

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tas_att_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the comment
	 */
    @Column(name="tas_att_comment", nullable = false)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the attachment
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fil_id", nullable = false)
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
	 * @return the desc
	 */
	@Column(name = "tas_att_desc", nullable = true)
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

  
	
}

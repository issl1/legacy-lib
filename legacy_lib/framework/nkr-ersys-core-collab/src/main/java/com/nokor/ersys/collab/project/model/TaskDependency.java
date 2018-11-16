package com.nokor.ersys.collab.project.model;

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

import org.seuksa.frmk.model.entity.EntityA;

/**
 * A (child) task  can depend on 1 or n (parent) tasks
 * A (parent) task can block     1 or n (children) tasks
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_task_dependency")
public class TaskDependency extends EntityA {
	/** */
	private static final long serialVersionUID = -2975495155651650263L;

	private Task child;
	private Task parent;
	private Date when;
	

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tas_dep_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    

	/**
	 * @return the child
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="child_tas_id", nullable = true)
	public Task getChild() {
		return child;
	}


	/**
	 * @param child the child to set
	 */
 	public void setChild(Task child) {
		this.child = child;
	}


	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_tas_id", nullable = true)
	public Task getParent() {
		return parent;
	}


	/**
	 * @param parent the parent to set
	 */
	public void setParent(Task parent) {
		this.parent = parent;
	}


	/**
	 * @return the when
	 */
	@Column(name = "tas_dep_when", nullable = false)
	public Date getWhen() {
		return when;
	}


	/**
	 * @param when the when to set
	 */
	public void setWhen(Date when) {
		this.when = when;
	}


}

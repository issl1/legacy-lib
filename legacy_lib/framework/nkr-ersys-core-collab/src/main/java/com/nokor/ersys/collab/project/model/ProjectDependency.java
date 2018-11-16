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
 * A (child) project  can depend on 1 or n (parent) projects
 * A (parent) project can block     1 or n (children) projects
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_project_dependency")
public class ProjectDependency extends EntityA {
	/** */
	private static final long serialVersionUID = -5526155081408739415L;

	private Project child;
	private Project parent;
	private Date when;
	


	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_dep_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    
	/**
	 * @return the child
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="child_prj_id", nullable = true)
	public Project getChild() {
		return child;
	}


	/**
	 * @param child the child to set
	 */
 	public void setChild(Project child) {
		this.child = child;
	}


	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_prj_id", nullable = true)
	public Project getParent() {
		return parent;
	}


	/**
	 * @param parent the parent to set
	 */
	public void setParent(Project parent) {
		this.parent = parent;
	}


	/**
	 * @return the when
	 */
	@Column(name = "prj_dep_when", nullable = false)
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

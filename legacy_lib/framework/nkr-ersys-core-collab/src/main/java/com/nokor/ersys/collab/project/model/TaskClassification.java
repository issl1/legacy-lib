package com.nokor.ersys.collab.project.model;

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

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Ex:
 * Domain: Documentation, QA, Development
 *    Sub-domain: fo, bo, ra
 *      ...
 *        Component: db, frmk, modules..
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_task_classification")
public class TaskClassification extends EntityRefA {
	/** */
	private static final long serialVersionUID = -3612958849908074536L;

	public final static long DEFAULT_DOMAIN_ID = 1L;

	private TaskClassification parent;
	private List<TaskClassification> children;

	

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tas_cla_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "tas_cla_code", nullable = true, length = 30)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "tas_cla_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "tas_cla_desc_en", nullable = false)
	@Override
	public String getDescEn() {
		return descEn;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_tas_cla_id", nullable = true)
	public TaskClassification getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(TaskClassification parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	public List<TaskClassification> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<TaskClassification> children) {
		this.children = children;
	}
	

    
}

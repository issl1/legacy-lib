package com.nokor.frmk.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_sec_control")
public class SecControl extends EntityRefA implements MSecControl {
	/** */
	private static final long serialVersionUID = -4950501295563362162L;
	
	private SecApplication application;
	private SecControl parent;
	private SecControl group; // Is used to group Controls
	private List<SecControlProfilePrivilege> controlProfilePrivileges;
	private List<SecControlPrivilege> controlPrivileges;
	private ESecControlType type;
	
    private List<SecControl> children;
    private List<SecControl> childrenInGroup;

    /**
     * 
     * @return
     */
    public static SecControl createInstance(ESecControlType type) {
    	SecControl ctl = EntityFactory.createInstance(SecControl.class);
    	ctl.setType(type);
        return ctl;
    }

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_ctl_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_ctl_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_ctl_desc", nullable = true)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "sec_ctl_desc_en", nullable = true)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the application
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_app_id", nullable = false)
	public SecApplication getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(SecApplication application) {
		this.application = application;
	}

	/**
	 * 
	 * @return parent the parent to set
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_sec_ctl_id", nullable = true)
	public SecControl getParent() {
		return parent;
	}

	public void setParent(SecControl parent) {
		this.parent = parent;
	}

	/**
	 * @return the group
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_sec_ctl_id", nullable = true)
	public SecControl getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(SecControl group) {
		this.group = group;
	}

	/**
	 * @return the controlProfilePrivileges
	 */
	@OneToMany(mappedBy = "control", fetch = FetchType.LAZY)
	public List<SecControlProfilePrivilege> getControlProfilePrivileges() {
		if (controlProfilePrivileges == null) {
			controlProfilePrivileges = new ArrayList<>();
		}
		return controlProfilePrivileges;
	}

	/**
	 * @param controlProfilePrivileges the controlProfilePrivileges to set
	 */
	public void setControlProfilePrivileges(List<SecControlProfilePrivilege> controlProfilePrivileges) {
		this.controlProfilePrivileges = controlProfilePrivileges;
	}
	
	/**
	 * @return the controlPrivileges
	 */
	@OneToMany(mappedBy = "control", fetch = FetchType.LAZY)
	public List<SecControlPrivilege> getControlPrivileges() {
		if (controlPrivileges == null) {
			controlPrivileges = new ArrayList<>();
		}
		return controlPrivileges;
	}

	/**
	 * @param controlPrivileges the controlPrivileges to set
	 */
	public void setControlPrivileges(List<SecControlPrivilege> controlPrivileges) {
		this.controlPrivileges = controlPrivileges;
	}
	
	/**
	 * @return the type
	 */
	@Column(name = "sec_ctl_typ_id", nullable = false)
    @Convert(converter = ESecControlType.class)
	public ESecControlType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ESecControlType type) {
		this.type = type;
	}

	/**
	 * @return the children
	 */
    @OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
    @OrderBy(value = "sort_index, id")
	public List<SecControl> getChildren() {
    	if (children == null) {
    		children = new ArrayList<>();
    	}
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<SecControl> children) {
		this.children = children;
	}

	/**
	 * @return the childrenInGroup
	 */
    @OneToMany(mappedBy = "group", fetch=FetchType.LAZY)
    @OrderBy(value = "sort_index, id")
	public List<SecControl> getChildrenInGroup() {
    	if (childrenInGroup == null) {
    		childrenInGroup = new ArrayList<>();
    	}
		return childrenInGroup;
	}

	/**
	 * @param childrenInGroup the childrenInGroup to set
	 */
	public void setChildrenInGroup(List<SecControl> childrenInGroup) {
		this.childrenInGroup = childrenInGroup;
	}
	
	
}

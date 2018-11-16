package com.nokor.ersys.collab.wkgroup.model;

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
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_wkg_group")
public class WkgGroup extends EntityA {
	/** */
	private static final long serialVersionUID = 5358161924822448349L;

	private String name;
	private String desc;
	private EWkgGroupType type;
	private WkgGroup parent;
	private Integer sortIndex;

	private List<WkgGroup> children;
	private List<WkgMember> members;


	/**
	 * 
	 * @return
	 */
	public static WkgGroup createInstance() {
        WkgGroup wkg = EntityFactory.createInstance(WkgGroup.class);
        return wkg;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkg_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the name
	 */
    @Column(name = "wkg_name", nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the desc
	 */
    @Column(name = "wkg_desc", nullable = true)
	public String getDesc() {
    	if (desc == null) {
    		return name;
    	}
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * @return the type
	 */
    @Column(name = "wkg_typ_id", nullable = false)
    @Convert(converter = EWkgGroupType.class)
	public EWkgGroupType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EWkgGroupType type) {
		this.type = type;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_wkg_id", nullable=true)
	public WkgGroup getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(WkgGroup parent) {
		this.parent = parent;
	}


	/**
	 * @return the children
	 */
    @OneToMany(mappedBy="parent")
	public List<WkgGroup> getChildren() {
    	if (children == null) {
    		children = new ArrayList<WkgGroup>();
    	}
		return children;
	}
  
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<WkgGroup> children) {
		this.children = children;
	}

	/**
     * @return the sortIndex
     */
    @Column(name = "sort_index", nullable = true)
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param sortIndex the sortIndex to set
     */
    public void setSortIndex(final Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

	/**
	 * @return the members
	 */
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	public List<WkgMember> getMembers() {
    	if (members == null) {
    		members = new ArrayList<WkgMember>();
    	}
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(List<WkgMember> members) {
		this.members = members;
	}
    
	
}

package com.nokor.common.app.menu.model;

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

import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;

/**
 * Menu domain object. Menu contains MenuItems.
 *
 * @author prasnar
 */
@Entity
@Table(name = "tu_menu")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MenuEntity extends EntityRefA implements MMenuEntity {
    /** */
	private static final long serialVersionUID = 5434705930935315258L;
	
	private SecApplication application;
    private EMenuType type;
    private SecControl control;
    
    private List<MenuItemEntity> menuItems = new ArrayList<MenuItemEntity>();

    /**
     * 
     * @return
     */
    public static MenuEntity createInstance(EMenuType type) {
    	MenuEntity mnu = EntityFactory.createInstance(MenuEntity.class);
    	mnu.setType(type);
        return mnu;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mnu_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    @Column(name = "mnu_code", nullable = false, length = 50)
	@Override
	public String getCode() {
		return code;
	}
    
    @Column(name = "mnu_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
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
	 * @return the type
	 */
	@Column(name = "typ_mnu_id", nullable = true)
    @Convert(converter = EMenuType.class)
	public EMenuType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EMenuType type) {
		this.type = type;
	}

	

	/**
	 * @return the control
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sec_ctl_id", nullable = true)
	public SecControl getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(SecControl control) {
		this.control = control;
	}


	/**
	 * @return the menuItems
	 */
    @OneToMany(mappedBy = "menu")
    @OrderBy(value = "sort_index, code")
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<MenuItemEntity> getMenuItems() {
		return menuItems;
	}

	/**
	 * @param menuItems the menuItems to set
	 */
	public void setMenuItems(List<MenuItemEntity> menuItems) {
		this.menuItems = menuItems;
	}


}

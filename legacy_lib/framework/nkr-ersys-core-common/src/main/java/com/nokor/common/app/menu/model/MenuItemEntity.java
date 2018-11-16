package com.nokor.common.app.menu.model;

import java.util.ArrayList;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.frmk.security.model.SecControl;

/**
 * Menu item domain object.
 *
 * @author prasnar
 */
@Entity
@Table(name = "tu_menu_item")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MenuItemEntity extends EntityRefA implements MMenuItemEntity {
    /** */
	private static final long serialVersionUID = 5890304602684729645L;

    private MenuEntity menu;
    private String action;
    private String iconPath;
    private MenuItemEntity parent;
    private Boolean isPopupWindow;
    private List<MenuItemEntity> children;
    private SecControl control;

    /**
     * 
     * @return
     */
    public static MenuItemEntity createInstance(MenuEntity menu) {
    	MenuItemEntity mnuIte = EntityFactory.createInstance(MenuItemEntity.class);
    	mnuIte.setMenu(menu);
        return mnuIte;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mnu_ite_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    @Column(name = "mnu_ite_code", nullable = false, length = 50)
	@Override
	public String getCode() {
		return code;
	}
    
    @Column(name = "mnu_ite_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
    
	/**
	 * @return the menu
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mnu_id", nullable = false)
	public MenuEntity getMenu() {
		return menu;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}

	/**
	 * @return the state
	 */

    @Column(name = "mnu_ite_action", nullable = true)
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the isPopupWindow
	 */
    @Column(name = "mnu_ite_is_popup", nullable = true)
	public Boolean isPopupWindow() {
		return  Boolean.TRUE.equals(isPopupWindow) ;
	}

	/**
	 * @param isPopupWindow the isPopupWindow to set
	 */
	public void setPopupWindow(Boolean isPopupWindow) {
		this.isPopupWindow = isPopupWindow;
	}

	/**
	 * @return the iconPath
	 */
    @Column(name = "mnu_ite_icon_path", nullable = true)
	public String getIconPath() {
		return iconPath;
	}

	/**
	 * @param iconPath the iconPath to set
	 */
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_mnu_ite_id", nullable = true)
	public MenuItemEntity getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(MenuItemEntity parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
    @OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
    @OrderBy(value = "sort_index, id")
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public List<MenuItemEntity> getChildren() {
    	if (children == null) {
    		children = new ArrayList<>();
    	}
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<MenuItemEntity> children) {
		this.children = children;
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
}

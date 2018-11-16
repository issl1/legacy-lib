package com.nokor.frmk.vaadin.ui.widget.breadcrumb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Link;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author ly.youhort
 */
public class BreadCrumb extends CustomComponent implements Button.ClickListener {
	
	private static final long serialVersionUID = 5594838672913295235L;

	public static interface AnimSpeed {
		String FAST = "fast";
		String SLOW = "slow";
		String NORMAL = "normal";
	}

	private static String LINKBUTTONSTYLE = "xbreadcrumbbutton";
	private static String LINKSTYLE = "xbreadcrumblink";

	private static String LINKSTYLEHOME = "home";

	private UList uList;
	private Map<Component, UList> componentToLi = new HashMap<Component, UList>();
	private boolean collapsible = true;
	private int collapsedWidth = 20;
	private String showAnimationSpeed = AnimSpeed.FAST;
	private String hideAnimationSpeed = AnimSpeed.FAST;
	private boolean useDefaultClickBehaviour = true;

	public BreadCrumb() {
		uList = new UList();
		uList.setStyleName("xbreadcrumbs");
		setCompositionRoot(uList);
	}

	/**
	 * Add new link(com.vaadin.ui.Button) to the end of the breadcrumb Button
	 * style will be set to BUTTON_LINK If default clickbehaviour is true, adds
	 * click listener to the link
	 * 
	 * @param link
	 */
	public void addLink(Button link) {
		link.setStyleName(BaseTheme.BUTTON_LINK);

		link.addStyleName(LINKBUTTONSTYLE);
		if (useDefaultClickBehaviour) {
			link.addClickListener(this);
		}
		UList li = createLi(link);
		if (uList.components.size() > 0) {
			int index = uList.components.size();
			while (index-- > 0) {
				Component newLast = uList.components.get(index);
				if (newLast.getStyleName().contains(("current"))) {
					newLast.removeStyleName("current");
					break;
				}
			}
		} else {
			link.addStyleName(LINKBUTTONSTYLE + "-" + LINKSTYLEHOME);
		}
		li.addStyleName("current");
		uList.addComponent(li);
		componentToLi.put(link, li);
		markAsDirty();
	}

	/**
	 * Add new link(com.vaadin.ui.Link) to the end of the breadcrumb
	 * 
	 * @param link
	 */
	public void addLink(Link link) {
		UList li = createLi(link);
		link.addStyleName(LINKSTYLE);
		if (uList.components.size() > 0) {
			int index = uList.components.size();
			while (index-- > 0) {
				Component newLast = uList.components.get(index);
				if (newLast.getStyleName().contains(("current"))) {
					newLast.removeStyleName("current");
					break;
				}
			}
		} else {
			link.addStyleName(LINKSTYLE + "-" + LINKSTYLEHOME);
		}
		li.addStyleName("current");
		uList.addComponent(li);
		componentToLi.put(link, li);
		markAsDirty();
	}

	/**
	 * @param link
	 * @return
	 */
	private UList createLi(Component link) {
		UList li = new UList();
		li.addComponent(link);
		return li;
	}

	/**
	 * selects the given input link
	 * 
	 * @param link
	 */
	public void select(Component link) {
		UList li = componentToLi.get(link);
		int index = uList.components.indexOf(li);
		while (uList.components.size() - 1 > index) {
			uList.removeComponent(uList.getLastComponent());
		}
		li.addStyleName("current");

	}

	/**
	 * selects the crumb at given input index
	 * 
	 * @param index
	 */
	public void select(int index) {
		Component li = uList.components.get(index);
		while (uList.components.size() - 1 > index) {
			uList.removeComponent(uList.getLastComponent());
		}
		li.addStyleName("current");
		markAsDirty();
	}

	/**
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("collapsible", collapsible);
		target.addAttribute("collapsedwidth", collapsedWidth);
		target.addAttribute("showspeed", showAnimationSpeed);
		target.addAttribute("hidespeed", hideAnimationSpeed);
	}*/

	public boolean isCollapsible() {
		return collapsible;
	}

	/**
	 * If set true, crumb will be collapsible and animated
	 * 
	 * @param collapsible
	 */

	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}

	public int getCollapsedWidth() {
		return collapsedWidth;
	}

	/**
	 * Determines the width of the link when it is collapsed
	 * 
	 * @param collapsedWidth
	 */
	public void setCollapsedWidth(int collapsedWidth) {
		this.collapsedWidth = collapsedWidth;
	}

	public String getShowAnimationSpeed() {
		return showAnimationSpeed;
	}

	/**
	 * Determines the speed when the linkis shown, use Interface AnimSpeed for
	 * input values
	 * 
	 * @param showAnimationSpeed
	 */
	public void setShowAnimationSpeed(String showAnimationSpeed) {
		this.showAnimationSpeed = showAnimationSpeed;
	}

	public String getHideAnimationSpeed() {
		return hideAnimationSpeed;
	}

	/**
	 * Determines the speed when the link is hide, use Interface AnimSpeed for
	 * input values
	 * 
	 * @param hideAnimationSpeed
	 */
	public void setHideAnimationSpeed(String hideAnimationSpeed) {
		this.hideAnimationSpeed = hideAnimationSpeed;
	}

	public void buttonClick(ClickEvent event) {
		select(event.getButton());

	}

	public boolean isUseDefaultClickBehaviour() {
		return useDefaultClickBehaviour;
	}

	/**
	 * If set, default click behaviour added to the links of type
	 * com.vaadin.ui.Button
	 * 
	 * @param useDefaultClickBehaviour
	 */

	public void setUseDefaultClickBehaviour(boolean useDefaultClickBehaviour) {
		this.useDefaultClickBehaviour = useDefaultClickBehaviour;
		Set<Component> buttons = componentToLi.keySet();
		for (Iterator<Component> iterator = buttons.iterator(); iterator.hasNext();) {
			Component component = iterator.next();
			if (component instanceof Button) {
				((Button) component).removeClickListener(this);
				if (useDefaultClickBehaviour) {
					((Button) component).addClickListener(this);
				}
			}
		}
		markAsDirty();
	}

	/**
	 * In order to make the link at location index to visible / invisible, this
	 * method must be used
	 * 
	 * @param visible
	 * @param index
	 */
	public void setLinkVisible(boolean visible, int index) {
		Component component = uList.components.get(index);
		if (component != null) {
			component.setVisible(visible);
			if (index == uList.components.size() - 1) {
				if (!visible) {
					component.removeStyleName("current");
					while (index-- > 0) {
						Component newLast = uList.components.get(index);
						if (newLast.isVisible()) {
							newLast.addStyleName("current");
							break;
						}
					}
				} else {
					component.addStyleName("current");
					while (index-- > 0) {
						Component newLast = uList.components.get(index);
						if (newLast.getStyleName().contains(("current"))) {
							newLast.removeStyleName("current");
							break;
						}
					}
				}
			}
			markAsDirty();
		}
	}

	/**
	 * In order to enable/disable the link at location index, this method must
	 * be used
	 * 
	 * @param enabled
	 * @param index
	 */
	public void setLinkEnabled(boolean enabled, int index) {
		Component component = uList.components.get(index);
		if (component != null) {
			component.setEnabled(enabled);
			markAsDirty();
		}
	}

	public int getIndexofLink(Component link) {
		Component li = componentToLi.get(link);
		return uList.components.indexOf(li);
	}
}

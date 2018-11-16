/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.province.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.address.province.detail.ProvinceFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BaseProvinceHolderPanel  extends AbstractTabsheetPanel implements View{
   /**	*/
	private static final long serialVersionUID = -1829215063365720686L;

	@Autowired
	private ProvinceTablePanel provinceTablePanel;
	@Autowired
	private ProvinceFormPanel provinceFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		provinceTablePanel.setMainPanel(this);
		provinceFormPanel.setCaption(I18N.message("province"));
		getTabSheet().setTablePanel(provinceTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void onAddEventClick() {
		provinceFormPanel.reset();
		getTabSheet().addFormPanel(provinceFormPanel);
		getTabSheet().setSelectedTab(provinceFormPanel);
		
	}
	
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(provinceFormPanel);
		initSelectedTab(provinceFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == provinceFormPanel) {
			provinceFormPanel.assignValues(provinceTablePanel.getItemSelectedId());
		} else if (selectedTab == provinceTablePanel && getTabSheet().isNeedRefresh()) {
			provinceTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

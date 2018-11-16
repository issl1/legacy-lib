/*
 * Created on 21/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.reftable.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.reftable.detail.RefTableFormPanel;
import com.nokor.ersys.vaadin.ui.referential.reftable.refdata.IRefTableDataForm;
import com.nokor.ersys.vaadin.ui.referential.reftable.refdata.IRefTableDataFormRender;
import com.nokor.ersys.vaadin.ui.referential.reftable.refdata.RefTableDataForm;
import com.nokor.ersys.vaadin.ui.referential.reftable.refdata.RefTableDataTablePanel;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;

import ru.xpoft.vaadin.VaadinView;

/**
 * RefTable Holder Panel.
 * 
 * @author pengleng.huot
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BaseRefTableHolderPanel.NAME)
public class BaseRefTableHolderPanel extends AbstractTabsheetPanel implements View {
	
	/**	 */
	private static final long serialVersionUID = 8254046231316566295L;
	public static final String NAME = "refdata";
	
	private final Logger logger = LoggerFactory.getLogger(BaseRefTableHolderPanel.class);
	private IRefTableDataFormRender formRender;
	
	@Autowired
	private RefTableTablePanel refTableTablePanel;
	@Autowired
	private RefTableFormPanel refTableFormPanel;
	@Autowired
	private RefTableDataTablePanel refTableDataTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();

		refTableTablePanel.setMainPanel(this);
		refTableFormPanel.setCaption(I18N.message("detail"));		
		refTableDataTablePanel.setCaption(I18N.message("ref.data"));	
		getTabSheet().setTablePanel(refTableTablePanel);		
	}
	
	public void setFormRender(RefTableDataFormRender formRender) {
		this.formRender = formRender;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == refTableFormPanel) {
			refTableFormPanel.assignValues(refTableTablePanel.getItemSelectedId());
		} else if (selectedTab == refTableTablePanel && getTabSheet().isNeedRefresh()) {
			refTableTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);		
	}

	@Override
	public void onAddEventClick() {	
	}

	/**
	 * 
	 * @param selectedId
	 * @param clazz
	 */
	private void addSubTab(Long selectedId, Class<?> clazz) {
		IRefTableDataForm form = null;
		
		if (formRender != null) {
			form = formRender.renderForm(clazz);
		}
		
		if (form == null) {
			RefTable refTable = REFDATA_SRV.getTable(clazz.getCanonicalName());
			form = new RefTableDataForm(refTable);
		}
		refTableDataTablePanel.assignValuesToControls(selectedId, form);
		getTabSheet().addFormPanel(refTableDataTablePanel);
	}
	
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(refTableFormPanel);
		
		try {
			addSubTab(refTableTablePanel.getItemSelectedId(), refTableTablePanel.getERefDataClass());
			initSelectedTab(refTableFormPanel);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	public void onViewSubTab() {
		if (refTableTablePanel.getSelectedItem() == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.view.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			getTabSheet().addFormPanel(refTableFormPanel);
			
			try {
				addSubTab(refTableTablePanel.getItemSelectedId(), refTableTablePanel.getERefDataClass());
				initSelectedTab(refTableDataTablePanel);
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}

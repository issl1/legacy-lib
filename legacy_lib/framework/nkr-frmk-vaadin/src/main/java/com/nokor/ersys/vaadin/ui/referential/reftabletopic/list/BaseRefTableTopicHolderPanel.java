package com.nokor.ersys.vaadin.ui.referential.reftabletopic.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
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
 * @author sayon.yen
 * WorkPlace Holder Panel
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BaseRefTableTopicHolderPanel.NAME)
public class BaseRefTableTopicHolderPanel extends AbstractTabsheetPanel implements View  {

	/** */
	private static final long serialVersionUID = -5222344887276688338L;

	public static final String NAME = "refTableTopic";
	
	private IRefTableDataFormRender formRender;
	
	@Autowired
	private RefTableTopicTreeTablePanel treeTable;
	
	@Autowired
	private RefTableFormPanel formPanel;
	
	@Autowired
	private RefTableDataTablePanel refTableDataTablePanel;
	
	/**
	 * WorkPlace Holder Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		treeTable.setMainPanel(this);
//		formPanel.setMainPanel(this);
		formPanel.setCaption(I18N.message("detail"));
		refTableDataTablePanel.setCaption(I18N.message("ref.data"));
		treeTable.setMargin(true);
		getTabSheet().setTablePanel(treeTable);
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		formPanel.reset();
		getTabSheet().addFormPanel(formPanel);
		getTabSheet().setSelectedTab(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(formPanel);
		
		try {
			addSubTab(treeTable.getItemSelectedId(), treeTable.getERefDataClass());
			initSelectedTab(formPanel);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == formPanel || selectedTab == refTableDataTablePanel) {
			formPanel.assignValues(treeTable.getItemSelectedId());
		} else if (selectedTab == treeTable && getTabSheet().isNeedRefresh()) {
			treeTable.refreshTreeTable();
		}
		getTabSheet().setSelectedTab(selectedTab);
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
	
	/**
	 * 
	 */
	public void onViewSubTab() {
		if (treeTable.getItemSelectedId() == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.view.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			getTabSheet().addFormPanel(formPanel);
			
			try {
				addSubTab(treeTable.getItemSelectedId(), treeTable.getERefDataClass());
				initSelectedTab(refTableDataTablePanel);
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}

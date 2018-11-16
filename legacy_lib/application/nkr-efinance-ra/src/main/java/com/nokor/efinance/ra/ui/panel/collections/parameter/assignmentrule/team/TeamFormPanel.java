package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.team;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Team form panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamFormPanel extends AbstractFormPanel implements SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = -6767388483343464423L;
	
	private EColTeam colTeam;
	private TextField txtCode;
	private TextField txtDescEn;
	private TextField txtDesc;
	private ERefDataComboBox<EColType> cbxColType;
	private TabSheet tabSheet;
	private VerticalLayout teamFormLayout;
	
	@Autowired
	private TeamDebtLevelTablePanel teamDebtLevelTablePanel;
	@Autowired
	private TeamStaffTablePanel teamStaffTablePanel;
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		colTeam.setCode(txtCode.getValue());
		colTeam.setDescEn(txtDescEn.getValue());
		colTeam.setDesc(txtDesc.getValue());
		colTeam.setColType(cbxColType.getSelectedEntity());
		return colTeam;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		
		cbxColType = new ERefDataComboBox<>(I18N.message("collection.type"), EColType.class);
		cbxColType.setWidth(200, Unit.PIXELS);
		
		teamDebtLevelTablePanel.setCaption(I18N.message("debt.level"));
		teamStaffTablePanel.setCaption(I18N.message("staff"));
	
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(cbxColType);
		
		teamFormLayout = ComponentFactory.getVerticalLayout();
		teamFormLayout.setSpacing(true);
		teamFormLayout.setMargin(true);
		teamFormLayout.addComponent(navigationPanel);
		teamFormLayout.addComponent(formLayout);
		
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		tabSheet.addTab(teamFormLayout, I18N.message("detail"));
		return tabSheet;
	}
	
	/**
	 * @param colTeamId
	 */
	public void assignValues(Long colTeamId) {
		super.reset();
		if (colTeamId != null) {
			colTeam = ENTITY_SRV.getById(EColTeam.class, colTeamId);
			txtCode.setValue(colTeam.getCode());
			txtDescEn.setValue(colTeam.getDescEn());
			txtDesc.setValue(colTeam.getDesc());
			cbxColType.setSelectedEntity(colTeam.getColType());
			displayTabs();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		EColTeam colTeam = (EColTeam) getEntity();
		ENTITY_SRV.saveOrUpdate(colTeam);
		displaySuccess();
		displayTabs();
	}
	
	/**
	 * 
	 */
	private void displayTabs() {
		tabSheet.setSelectedTab(teamFormLayout);
		tabSheet.addTab(teamDebtLevelTablePanel);
		tabSheet.addTab(teamStaffTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		colTeam = new EColTeam();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbxColType.setSelectedEntity(null);
		tabSheet.removeComponent(teamDebtLevelTablePanel);
		tabSheet.removeComponent(teamStaffTablePanel);
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		super.reset();
		if (colTeam != null) {
			if (tabSheet.getSelectedTab() == teamFormLayout) {
				assignValues(colTeam.getId());
			} else if (tabSheet.getSelectedTab() == teamDebtLevelTablePanel) {
				teamDebtLevelTablePanel.assignValues(colTeam.getId());
			} else if (tabSheet.getSelectedTab() == teamStaffTablePanel) {
				teamStaffTablePanel.assignValues(colTeam.getId());
			}
		}
	}	
	
}

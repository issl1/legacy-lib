package com.nokor.efinance.gui.ui.panel.marketing.team;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.marketing.model.Team;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.marketing.team.employee.TeamEmployeeFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamMarketingFormPanel extends AbstractFormPanel implements SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = -2667723626427205660L;
	
	private Team mktTeam;
	private TextField txtDesc;
	private TabSheet tabSheet;
	
	private TeamEmployeeFormPanel teamEmployeeFormPanel;
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		mktTeam.setDescription(txtDesc.getValue());
		return mktTeam;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtDesc = ComponentFactory.getTextField(60, 180);
		
		teamEmployeeFormPanel = new TeamEmployeeFormPanel();
	
		HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setSpacing(true);
		formLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("desc"));
		formLayout.addComponent(txtDesc);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		
		VerticalLayout teamLayout = ComponentFactory.getVerticalLayout();
		teamLayout.setSpacing(true);
		teamLayout.setMargin(true);
		teamLayout.addComponent(navigationPanel);
		teamLayout.addComponent(formLayout);
		
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		tabSheet.addTab(teamLayout, I18N.message("detail"));
		return tabSheet;
	}
	
	/**
	 * @param teamId
	 */
	public void assignValues(Long teamId) {
		this.reset();
		if (teamId != null) {
			mktTeam = ENTITY_SRV.getById(Team.class, teamId);
			txtDesc.setValue(mktTeam.getDescription());
			displayTabs();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		Team team = (Team) getEntity();
		ENTITY_SRV.saveOrUpdate(team);
		displaySuccess();
		displayTabs();
	}
	
	/**
	 * 
	 */
	private void displayTabs() {
		teamEmployeeFormPanel.reset();
		tabSheet.addTab(teamEmployeeFormPanel, I18N.message("employee"));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		mktTeam = Team.createInstance();
		txtDesc.setValue(StringUtils.EMPTY);
		tabSheet.removeComponent(teamEmployeeFormPanel);
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDesc, "description");
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		super.reset();
		if (mktTeam != null) {
			if (tabSheet.getSelectedTab() == teamEmployeeFormPanel) {
				teamEmployeeFormPanel.assignValues(mktTeam.getId());
			}
		}
	}	
	
}

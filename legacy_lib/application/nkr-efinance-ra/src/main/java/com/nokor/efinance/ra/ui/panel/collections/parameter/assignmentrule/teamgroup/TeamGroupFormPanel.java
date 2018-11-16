package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.teamgroup;

import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.RefDataId;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColTeamGroup;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Team and Group form panel in assignment rule
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamGroupFormPanel extends AbstractFormPanel {
	
	/** */
	private static final long serialVersionUID = 9137277887318682036L;
	
	private EColTeamGroup colTeamGroup;
	private TextField txtDebtLevel;
	private SecUserComboBox cbxTeamLeader; 
	private EntityRefComboBox<EColGroup> cbxGroup;
	private EntityRefComboBox<EColTeam> cbxTeam;
	private TextArea txtRemark;
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		colTeamGroup.setTeam(cbxTeam.getSelectedEntity() != null ? cbxTeam.getSelectedEntity() : null);
		colTeamGroup.setGroup(cbxGroup.getSelectedEntity() != null ? cbxGroup.getSelectedEntity() : null);
		colTeamGroup.setDeptLevel(getInteger(txtDebtLevel) != null ? getInteger(txtDebtLevel) : 0);
		colTeamGroup.setTeamLeader(cbxTeamLeader.getSelectedEntity() != null ? cbxTeamLeader.getSelectedEntity() : null);
		colTeamGroup.setRemark(txtRemark.getValue());
		return colTeamGroup;
	}
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T>  getEntityRefComboBox(String caption, BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>(I18N.message(caption));
		comboBox.setWidth(200, Unit.PIXELS);
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		return comboBox;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxTeam = getEntityRefComboBox("team", new BaseRestrictions<>(EColTeam.class));
		cbxGroup = getEntityRefComboBox("group", new BaseRestrictions<>(EColGroup.class));
		cbxTeamLeader = new SecUserComboBox(I18N.message("team.leader"), getSecUsers());
		cbxTeamLeader.setWidth(200, Unit.PIXELS);
		txtDebtLevel = ComponentFactory.getTextField("debt.level", false, 60, 200);
		txtRemark = ComponentFactory.getTextArea("remark", false, 300, 100);
		VerticalLayout teamLayout = ComponentFactory.getVerticalLayout();
		teamLayout.setSpacing(true);
		teamLayout.addComponent(new FormLayout(cbxTeam));
		teamLayout.addComponent(new FormLayout(cbxGroup));
		teamLayout.addComponent(new FormLayout(txtDebtLevel));
		teamLayout.addComponent(new FormLayout(cbxTeamLeader));
		teamLayout.addComponent(new FormLayout(txtRemark));
		Panel mainPanel = ComponentFactory.getPanel();
		mainPanel.setSizeFull();
		mainPanel.setContent(teamLayout);
		return mainPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<SecUser> getSecUsers() {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * @param colTeamGroupID
	 */
	public void assignValues(Long colTeamGroupID) {
		super.reset();
		if (colTeamGroupID != null) {
			colTeamGroup = ENTITY_SRV.getById(EColTeamGroup.class, colTeamGroupID);
			cbxTeam.setSelectedEntity(colTeamGroup.getTeam() != null ? colTeamGroup.getTeam() : null);
			cbxGroup.setSelectedEntity(colTeamGroup.getGroup() != null ? colTeamGroup.getGroup() : null);
			txtDebtLevel.setValue(getDefaultString(colTeamGroup.getDeptLevel()));
			cbxTeamLeader.setSelectedEntity(colTeamGroup.getTeamLeader() != null ? colTeamGroup.getTeamLeader() : null);
			txtRemark.setValue(colTeamGroup.getRemark());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		colTeamGroup = new EColTeamGroup();
		txtDebtLevel.setValue("");
		txtRemark.setValue("");
		cbxTeam.setSelectedEntity(null);
		cbxGroup.setSelectedEntity(null);
		cbxTeamLeader.setSelectedEntity(null);
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkIntegerField(txtDebtLevel, I18N.message("debt.level"));
		return errors.isEmpty();
	}
}

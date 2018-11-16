package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.teamgroup;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColTeamGroup;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Team and Group search panel in collection
 * @author uhout.cheng
 */
public class TeamGroupSearchPanel extends AbstractSearchPanel<EColTeamGroup> implements CollectionEntityField {

	/** */
	private static final long serialVersionUID = -5083994045612412451L;

	private EntityRefComboBox<EColGroup> cbxGroup;
	private EntityRefComboBox<EColTeam> cbxTeam;
    
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
     * 
     * @param teamGroupTablePanel
     */
	public TeamGroupSearchPanel(TeamGroupTablePanel teamGroupTablePanel) {
		super(I18N.message("search"), teamGroupTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxTeam.setSelectedEntity(null);
		cbxGroup.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		cbxTeam = getEntityRefComboBox("team", new BaseRestrictions<>(EColTeam.class));
		cbxGroup = getEntityRefComboBox("group", new BaseRestrictions<>(EColGroup.class));
		HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(new FormLayout(cbxTeam));
        mainLayout.addComponent(new FormLayout(cbxGroup));
		return mainLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<EColTeamGroup> getRestrictions() {		
		BaseRestrictions<EColTeamGroup> restrictions = new BaseRestrictions<>(EColTeamGroup.class);	
		if (cbxTeam.getSelectedEntity() != null) {
			restrictions.addAssociation(TEAM, "tm", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("tm." + ID, cbxTeam.getSelectedEntity().getId()));
		}
		if (cbxGroup.getSelectedEntity() != null) {
			restrictions.addAssociation(GROUP, "grp", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("grp." + ID, cbxGroup.getSelectedEntity().getId()));
		}
		restrictions.addOrder(Order.desc(ID));
		return restrictions;
	}

}

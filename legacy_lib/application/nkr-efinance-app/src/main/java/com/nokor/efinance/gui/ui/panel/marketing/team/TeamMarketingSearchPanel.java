package com.nokor.efinance.gui.ui.panel.marketing.team;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.marketing.model.Team;
import com.nokor.efinance.core.marketing.service.TeamRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 */
public class TeamMarketingSearchPanel extends AbstractSearchPanel<Team> {

	/** */
	private static final long serialVersionUID = 2823779011073946977L;
	
	private TextField txtDescription;
	
    /**
     * 
     * @param teamTablePanel
     */
	public TeamMarketingSearchPanel(TeamMarketingTablePanel teamTablePanel) {
		super(I18N.message("filters"), teamTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDescription.setValue(StringUtils.EMPTY);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		txtDescription = ComponentFactory.getTextField(60, 180);
		
		HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(ComponentLayoutFactory.getLabelCaption("desc"));
        mainLayout.addComponent(txtDescription);
		return mainLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Team> getRestrictions() {		
		TeamRestriction restrictions = new TeamRestriction();
		restrictions.setDescription(txtDescription.getValue());
		return restrictions;
	}

}

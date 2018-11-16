package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.callcenterresult;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.callcenter.model.ECallCenterResultRestriction;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class CallCenterResultsSearchPanel extends AbstractSearchPanel<ECallCenterResult> implements CollectionEntityField {

    /** */
	private static final long serialVersionUID = -6598347101493060541L;

	private TextField txtCode;
	private TextField txtDesc;

	/**
     * 
     * @param colStatusTablePanel
     */
	public CallCenterResultsSearchPanel(CallCenterResultsTablePanel colStatusTablePanel) {
		super(I18N.message("search"), colStatusTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue(StringUtils.EMPTY);
		txtDesc.setValue(StringUtils.EMPTY);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		txtCode = ComponentFactory.getTextField("code", false, 100, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 100, 200);
		
		HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        
        mainLayout.addComponent(new FormLayout(txtCode));
        mainLayout.addComponent(new FormLayout(txtDesc));
		return mainLayout;
	}
		
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<ECallCenterResult> getRestrictions() {		
		ECallCenterResultRestriction restrictions = new ECallCenterResultRestriction();
		restrictions.setCode(txtCode.getValue());
		restrictions.setCode(txtDesc.getValue());
		return restrictions;
	}
	

}

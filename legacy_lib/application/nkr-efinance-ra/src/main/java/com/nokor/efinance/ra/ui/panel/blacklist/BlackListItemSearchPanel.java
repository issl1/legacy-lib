package com.nokor.efinance.ra.ui.panel.blacklist;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.BlackListItem;
import com.nokor.efinance.core.common.reference.model.MBlackListItem;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class BlackListItemSearchPanel extends AbstractSearchPanel<BlackListItem> implements MBlackListItem {

	/** */
	private static final long serialVersionUID = -4478589367053917211L;
	
	private TextField txtIdNumber;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private TextField txtPhoneNumber;
	
	/**
	 * 
	 * @param blackListItemTablePanel
	 */
	public BlackListItemSearchPanel(BlackListItemTablePanel blackListItemTablePanel) {
		super(I18N.message("blacklist.item"), blackListItemTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtIdNumber.setValue("");
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		txtPhoneNumber.setValue("");
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	private FormLayout getFormLayout(Component component) {
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(component);
		formLayout.setStyleName("myform-align-left");
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtIdNumber = ComponentFactory.getTextField("id.no", false, 60, 150);        
		txtFirstNameEn = ComponentFactory.getTextField("firstname.en", false, 60, 150);
		txtLastNameEn = ComponentFactory.getTextField("lastname.en", false, 60, 150);
		txtPhoneNumber = ComponentFactory.getTextField("phone.number", false, 20, 130);
		
		HorizontalLayout firstLayout = new HorizontalLayout();
        firstLayout.setSpacing(true);
        firstLayout.addComponent(getFormLayout(txtIdNumber));
        firstLayout.addComponent(getFormLayout(txtFirstNameEn));
        firstLayout.addComponent(getFormLayout(txtLastNameEn));
        firstLayout.addComponent(getFormLayout(txtPhoneNumber));

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(firstLayout);
        
		return mainLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<BlackListItem> getRestrictions() {
		
		BaseRestrictions<BlackListItem> restrictions = new BaseRestrictions<>(BlackListItem.class);
		
		if (StringUtils.isNotEmpty(txtIdNumber.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(IDNUMBER, txtIdNumber.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(FIRSTNAMEEN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(LASTNAMEEN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtPhoneNumber.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(MOBILEPERSO, txtPhoneNumber.getValue(), MatchMode.ANYWHERE));
		}		
		restrictions.addOrder(Order.asc(ID));
		
		return restrictions;	
	}
}

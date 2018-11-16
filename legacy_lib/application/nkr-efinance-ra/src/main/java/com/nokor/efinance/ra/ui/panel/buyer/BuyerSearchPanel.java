package com.nokor.efinance.ra.ui.panel.buyer;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.auction.model.Buyer;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Buyer Search Panel
 * @author bunlong.taing
 *
 */
public class BuyerSearchPanel extends AbstractSearchPanel<Buyer> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -7729685862089223574L;
	
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private TextField txtTelephone;

	public BuyerSearchPanel(AbstractTablePanel<Buyer> tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtFirstNameEn = ComponentFactory.getTextField("firstname.en", false, 50, 200);
		txtLastNameEn = ComponentFactory.getTextField("lastname.en", false, 50, 200);
		txtTelephone = ComponentFactory.getTextField("telephone", false, 50, 200);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(new FormLayout(txtFirstNameEn));
		horizontalLayout.addComponent(new FormLayout(txtLastNameEn));
		horizontalLayout.addComponent(new FormLayout(txtTelephone));
		
		return horizontalLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Buyer> getRestrictions() {
		BaseRestrictions<Buyer> restrictions = new BaseRestrictions<Buyer>(Buyer.class);
		
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(LAST_NAME_EN, txtLastNameEn.getValue()));
		}
		if (StringUtils.isNotEmpty(txtTelephone.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(TELEPHONE, txtTelephone.getValue()));
		}
		
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		txtTelephone.setValue("");
	}

}

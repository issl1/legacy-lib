package com.nokor.ersys.core.finance.bank.list;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.core.finance.model.Bank;
import com.nokor.ersys.core.finance.model.MBank;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * BankInfo Search Panel
 * @author bunlong.taing
 *
 */
public class BankSearchPanel extends AbstractSearchPanel<Bank> implements MBank {

	/** */
	private static final long serialVersionUID = 8324309750903561788L;
	
	private TextField txtDescEn;

	/**
	 * @param tablePanel
	 */
	public BankSearchPanel(BankTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtDescEn = ComponentFactory.getTextField("name.en", false, 100, 200);
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtDescEn);
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Bank> getRestrictions() {
		BaseRestrictions<Bank> restrictions = new BaseRestrictions<Bank>(Bank.class);	
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(Bank.DESC, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDescEn.setValue("");
	}

}

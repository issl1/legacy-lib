package com.nokor.efinance.ra.ui.panel.finproduct.term.list;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.financial.model.Term;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class TermSearchPanel extends AbstractSearchPanel<Term> {
	/** */
	private static final long serialVersionUID = -5019544043911356215L;
	
	private TextField txtDescEn;

	public TermSearchPanel(TermTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		Label lblSearchText = ComponentFactory.getLabel("desc");
		txtDescEn = ComponentFactory.getTextField(100, 200);
		
		GridLayout content = new GridLayout(2, 1);
		content.setSpacing(true);
		content.addComponent(lblSearchText);
		content.addComponent(txtDescEn);
		content.setComponentAlignment(lblSearchText, Alignment.MIDDLE_LEFT);
		return content;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Term> getRestrictions() {
		BaseRestrictions<Term> restrictions = new BaseRestrictions<Term>(Term.class);
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) {
			restrictions.addCriterion(Restrictions.or(Restrictions.ilike(Term.DESC, txtDescEn.getValue(), MatchMode.ANYWHERE),
					Restrictions.ilike(Term.DESCEN, txtDescEn.getValue(), MatchMode.ANYWHERE)));
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

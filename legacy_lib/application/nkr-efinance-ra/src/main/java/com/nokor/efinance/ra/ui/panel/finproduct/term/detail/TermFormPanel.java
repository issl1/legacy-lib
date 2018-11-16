package com.nokor.efinance.ra.ui.panel.finproduct.term.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.Term;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Term Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TermFormPanel extends AbstractFormPanel {
	/** */
	private static final long serialVersionUID = 2011916207540333115L;
	
	private TextField txtDescEn;
	private TextField txtValue;
	
	private Term term;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtValue = ComponentFactory.getTextField("value", true, 3, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 100, 200);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtValue);
		formLayout.addComponent(txtDescEn);
		
		return formLayout;
	}
	
	/**
	 * Assign value to form
	 * @param termId
	 */
	public void assignValues(Long termId) {
		reset();
		if (termId != null) {
			term = ENTITY_SRV.getById(Term.class, termId);
			txtValue.setValue(term.getValue() != null ? String.valueOf(term.getValue()) : "");
			txtDescEn.setValue(term.getDescEn());
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		term = new Term();
		txtValue.setValue("");
		txtDescEn.setValue("");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkIntegerField(txtValue, "value");
		checkMandatoryField(txtValue, "value");
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		term.setValue(MyNumberUtils.getInteger(txtValue.getValue(), 0));
		term.setDesc(txtDescEn.getValue());
		term.setDescEn(txtDescEn.getValue());
		return term;
	}

}

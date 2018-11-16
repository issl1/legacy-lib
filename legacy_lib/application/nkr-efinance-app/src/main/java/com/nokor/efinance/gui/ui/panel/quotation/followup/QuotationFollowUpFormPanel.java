package com.nokor.efinance.gui.ui.panel.quotation.followup;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.panel.HistoryStatusPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;

/**
 * 
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QuotationFollowUpFormPanel extends AbstractFormPanel {

	private static final long serialVersionUID = -5353663324165007666L;
	private HistoryStatusPanel historyStatusPanel; 
    @PostConstruct
	public void PostConstruct() {
    	super.init();
        setCaption(I18N.message("quotations.follow.up"));
	}
	@Override
	protected com.vaadin.ui.Component createForm() {
		historyStatusPanel = new HistoryStatusPanel();
		// TODO Auto-generated method stub
		return historyStatusPanel;
	}
	@Override
	protected Entity getEntity() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @param asmakId
	 */
	public void assignValues(Long quotationID) {
		if (quotationID != null) {
			Quotation quotation = ENTITY_SRV.getById(Quotation.class, quotationID);
			historyStatusPanel.assignValues(quotation);
		}
	}
}

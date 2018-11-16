package com.nokor.efinance.gui.ui.panel.payment.integration.file.list;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.payment.model.EPaymentFileFormat;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.PaymentFileRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

/**
 * File Integration Search Panel
 * @author bunlong.taing
 */
public class PaymentFileSearchPanel extends AbstractSearchPanel<PaymentFile> {
	/** */
	private static final long serialVersionUID = 4047104346325100264L;
	
	private ERefDataComboBox<EPaymentFileFormat> cbxPaymentFileFormat;
	private AutoDateField dfUploadDate;

	/**
	 * File Integration Search Panel
	 * @param tablePanel
	 */
	public PaymentFileSearchPanel(PaymentFileTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		dfUploadDate = ComponentFactory.getAutoDateField();
		
		cbxPaymentFileFormat = new ERefDataComboBox<>(EPaymentFileFormat.values());
		cbxPaymentFileFormat.setWidth(200, Unit.PIXELS);
		
		Label lblFileFormat = ComponentFactory.getLabel("file.format");
		Label lblSearchText = ComponentFactory.getLabel("upload.date");
		
		GridLayout content = new GridLayout(4, 1);
		content.setSpacing(true);
		content.addComponent(lblFileFormat);
		content.addComponent(cbxPaymentFileFormat);
		content.addComponent(lblSearchText);
		content.addComponent(dfUploadDate);
		
		content.setComponentAlignment(lblFileFormat, Alignment.MIDDLE_LEFT);
		content.setComponentAlignment(lblSearchText, Alignment.MIDDLE_LEFT);
		
		return content;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<PaymentFile> getRestrictions() {
		PaymentFileRestriction restrictions = new PaymentFileRestriction();
		restrictions.setFormat(cbxPaymentFileFormat.getSelectedEntity());
		if (dfUploadDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(PaymentFileItem.CREATEDATE, DateUtils.getDateAtBeginningOfDay(dfUploadDate.getValue())));
			restrictions.addCriterion(Restrictions.le(PaymentFileItem.CREATEDATE, DateUtils.getDateAtEndOfDay(dfUploadDate.getValue())));
		} 
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxPaymentFileFormat.setSelectedEntity(null);
		dfUploadDate.setValue(null);
	}

}

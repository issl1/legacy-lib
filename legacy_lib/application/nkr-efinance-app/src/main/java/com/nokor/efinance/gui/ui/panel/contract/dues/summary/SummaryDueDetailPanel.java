package com.nokor.efinance.gui.ui.panel.contract.dues.summary;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.LabelFormCustomLayout;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class SummaryDueDetailPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 5098572802610310816L;
	
	private Label lblNextDueDate;
	private Label lblPaidRI;
	private Label lblODI;
	private Label lblODM;
	private VerticalLayout vLayout;
	
	/**
	 * 
	 * @param deleget
	 */
	public SummaryDueDetailPanel() {
		lblNextDueDate = getLabelValue();
		lblPaidRI = getLabelValue();
		lblODI = getLabelValue();
		lblODM = getLabelValue();
		
		vLayout = new VerticalLayout();
	  
		addComponent(vLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getDetailLayout() {
		VerticalLayout vLayout = new VerticalLayout();
		
		HorizontalLayout leftHorLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		leftHorLayout.addComponent(new LabelFormCustomLayout("next.due.date", lblNextDueDate.getValue(), 90, 70));
		leftHorLayout.addComponent(new LabelFormCustomLayout("pi.ri", lblPaidRI.getValue(), 40, 70));
		leftHorLayout.addComponent(new LabelFormCustomLayout("odi", lblODI.getValue(), 40, 40));
		leftHorLayout.addComponent(new LabelFormCustomLayout("odm", lblODM.getValue(), 40, 40));
		vLayout.addComponent(leftHorLayout);
		
		return vLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.reset();
		if (contract != null) {
			Collection col = contract.getCollection();
			
			if (col != null) {
				lblNextDueDate.setValue(getDescription(getDateFormat(col.getNextDueDate())));
				lblODI.setValue(getDescription(getDefaultString(MyNumberUtils.getInteger(col.getNbInstallmentsInOverdue()))));
				lblODM.setValue(getDescription(getDefaultString(MyNumberUtils.getInteger(col.getDebtLevel()))));
				
				double pi = 0;
				double ri = 0;
				pi = MyNumberUtils.getDouble(col.getPartialPaidInstallment());
				ri = contract.getTerm() - pi;
				String paidInstallment = AmountUtils.format(pi);
				String remainingInstallment = AmountUtils.format(ri);
				lblPaidRI.setValue(getDescription(paidInstallment + "/" + remainingInstallment));
			}
		} 
		vLayout.removeAllComponents();
		vLayout.addComponent(getDetailLayout());
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		lblNextDueDate.setValue(StringUtils.EMPTY);
		lblPaidRI.setValue(StringUtils.EMPTY);
		lblODI.setValue(StringUtils.EMPTY);
		lblODM.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : StringUtils.EMPTY;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = ComponentFactory.getHtmlLabel(StringUtils.EMPTY);
		return label;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? StringUtils.EMPTY : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
}

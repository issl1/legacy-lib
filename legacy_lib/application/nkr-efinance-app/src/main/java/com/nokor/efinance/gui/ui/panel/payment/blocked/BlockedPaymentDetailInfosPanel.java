package com.nokor.efinance.gui.ui.panel.payment.blocked;

import org.apache.commons.lang3.StringUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class BlockedPaymentDetailInfosPanel extends VerticalLayout implements MPayment, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 5192035993972639729L;

	private NumberField txtNbOfPayment;
	private NumberField txtAmount1;
	private NumberField txtAmount2;
	private NumberField txtTotal;
	private TextField txtLesseeFullName;
	private TextField txtPrimaryPhoneNO;
	private TextField txtOtherPhoneNO;
	
	private TabSheet mainTab;
	
	/**
	 * 
	 * @param wkfStatus
	 */
	public BlockedPaymentDetailInfosPanel(EWkfStatus wkfStatus) {
		mainTab = new TabSheet();
		mainTab.addTab(new VerticalLayout(), I18N.message("summary"));
		if (PaymentFileWkfStatus.OVER.equals(wkfStatus)) {
			mainTab.addTab(new RefundsLayout(), I18N.message("refunds"));
		}
		
		VerticalLayout layout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, true, true), true);
		layout.addComponent(createTopLayout());
		layout.addComponent(mainTab);
		
		Panel panel = new Panel("<b style=\"color:#3B5998\">" + I18N.message("infos") + "</b>", layout);
		panel.setCaptionAsHtml(true);
		addComponent(panel);
	}
	
	/**
	 * 
	 * @return
	 */
	private Component createTopLayout() {
		txtNbOfPayment = ComponentFactory.getNumberField();
		txtAmount1 = ComponentFactory.getNumberField();
		txtAmount2 = ComponentFactory.getNumberField();
		txtTotal = ComponentFactory.getNumberField();
		txtLesseeFullName = ComponentFactory.getTextField();
		txtPrimaryPhoneNO = ComponentFactory.getTextField();
		txtOtherPhoneNO = ComponentFactory.getTextField();
		
		txtNbOfPayment.setEnabled(false);
		txtAmount1.setEnabled(false);
		txtAmount2.setEnabled(false);
		txtTotal.setEnabled(false);
		txtLesseeFullName.setEnabled(false);
		txtPrimaryPhoneNO.setEnabled(false);
		txtOtherPhoneNO.setEnabled(false);
		
		Label lblNbOfPayment = ComponentFactory.getLabel("nb.of.payment");
		Label lblAmount1 = ComponentFactory.getLabel("amount1");
		Label lblAmount2 = ComponentFactory.getLabel("amount2");
		Label lblTotal = ComponentFactory.getLabel("total");
		Label lblLesseeFullName = ComponentFactory.getLabel("lessee.fullname");
		Label lblPrimaryPhoneNO = ComponentFactory.getLabel("primary.phone.no");
		Label lblOtherPhoneNO = ComponentFactory.getLabel("other.phone.no");
		
		GridLayout gridLayout = ComponentLayoutFactory.getGridLayout(new MarginInfo(true, false, false, false), 8, 2);
		gridLayout.addComponent(lblNbOfPayment);
		gridLayout.addComponent(txtNbOfPayment);
		gridLayout.addComponent(lblAmount1);
		gridLayout.addComponent(txtAmount1);
		gridLayout.addComponent(lblAmount2);
		gridLayout.addComponent(txtAmount2);
		gridLayout.addComponent(lblTotal);
		gridLayout.addComponent(txtTotal);
		
		gridLayout.addComponent(lblLesseeFullName);
		gridLayout.addComponent(txtLesseeFullName);
		gridLayout.addComponent(lblPrimaryPhoneNO);
		gridLayout.addComponent(txtPrimaryPhoneNO);
		gridLayout.addComponent(lblOtherPhoneNO);
		gridLayout.addComponent(txtOtherPhoneNO);
		
		gridLayout.setComponentAlignment(lblNbOfPayment, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblAmount1, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblAmount2, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblTotal, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblLesseeFullName, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblPrimaryPhoneNO, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblOtherPhoneNO, Alignment.MIDDLE_RIGHT);
		
		return gridLayout;
	}
	
	/**
	 * 
	 * @param fileItem
	 */
	public void assignValue(PaymentFileItem fileItem) {
		reset();
		Contract contract = CONT_SRV.getByReference(fileItem.getCustomerRef1());
		if (contract != null) {
			Applicant app = contract.getApplicant();
			txtLesseeFullName.setValue(app.getNameLocale());
			Individual ind = app.getIndividual();
			txtPrimaryPhoneNO.setValue(ind == null ? StringUtils.EMPTY : ind.getIndividualPrimaryContactInfo());
			txtOtherPhoneNO.setValue(ind == null ? StringUtils.EMPTY : ind.getIndividualSecondaryContactInfo());
		}
	}
	
	/**
	 * 
	 */
	public void reset() {
		txtNbOfPayment.setValue(StringUtils.EMPTY);
		txtAmount1.setValue(StringUtils.EMPTY);
		txtAmount2.setValue(StringUtils.EMPTY);
		txtTotal.setValue(StringUtils.EMPTY);
		txtLesseeFullName.setValue(StringUtils.EMPTY);
		txtPrimaryPhoneNO.setValue(StringUtils.EMPTY);
		txtOtherPhoneNO.setValue(StringUtils.EMPTY);
	}
	
}

package com.nokor.efinance.gui.ui.panel.cashflow;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * 
 * @author sok.vina
 *
 */
public class CashflowSearchPanel extends AbstractSearchPanel<Cashflow> implements CashflowEntityField {

	private static final long serialVersionUID = -6223163592840947320L;
	
	private ERefDataComboBox<ETreasuryType> cbxTreasuryType;
	private ERefDataComboBox<ECashflowType> cbxType;
	//private ERefDataComboBox<EPaymentMethod> cbxPaymentMethod;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private TextField txtContractReference;
	
	public CashflowSearchPanel(CashflowTablePanel cashflowTablePanel) {
		super(I18N.message("search"), cashflowTablePanel);
	}
	
	@Override
	protected void reset() {
		cbxTreasuryType.setSelectedEntity(null);
		cbxType.setSelectedEntity(null);
		cbxPaymentMethod.setSelectedEntity(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}

	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(14, 3);
		gridLayout.setSpacing(true);
		cbxTreasuryType = new ERefDataComboBox<ETreasuryType>(ETreasuryType.class);
		cbxType = new ERefDataComboBox<ECashflowType>(ECashflowType.class);
		cbxPaymentMethod = new EntityRefComboBox<>();
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.renderer();
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfEndDate = ComponentFactory.getAutoDateField("", false);   
        
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("treasury")), iCol++, 0);
        gridLayout.addComponent(cbxTreasuryType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("type")), iCol++, 0);
        gridLayout.addComponent(cbxType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 0);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 1);
        gridLayout.addComponent(dfStartDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<Cashflow> getRestrictions() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxTreasuryType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, cbxTreasuryType.getSelectedEntity()));
		}
		if (cbxType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, cbxType.getSelectedEntity()));
		}
		if (cbxPaymentMethod.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(PAYMENT_METHOD +"."+ ID, cbxPaymentMethod.getSelectedEntity().getId()));
		}		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		restrictions.addOrder(Order.desc("cont." + REFERENCE));
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
		return restrictions;
	}
}

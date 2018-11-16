package com.nokor.efinance.gui.ui.panel.report.uwrejectreport;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.SupportDecision;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * @author buntha.chea
 */
public class UWRejectReportSearchPanel extends AbstractSearchPanel<Quotation> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -2663311603012223797L;
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private ComboBox cbxRejectTermFrom;
	private ComboBox cbxRejectTermTo;
	private ComboBox cbxRejectReson;
	private ComboBox cbxFieldCheck;
	private ComboBox cbxAdvancePayment;
	private ComboBox cbxTerm;
	private TextField txtId;
	
	List<SupportDecision> supportDecisions = DataReference.getInstance().getSupportDecisions(QuotationWkfStatus.REJ);
	//private AutoDateField dfEndDate;
	
	/**
	 * @param tablePanel
	 */
	public UWRejectReportSearchPanel(UWRejectReportTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		
		cbxRejectTermFrom = new ComboBox(I18N.message("reject.term.from"));
		cbxRejectTermFrom.setWidth("150px");
		cbxRejectTermFrom.addItem(12);
		cbxRejectTermFrom.setItemCaption(12, I18N.message("12month"));
		cbxRejectTermFrom.addItem(24);
		cbxRejectTermFrom.setItemCaption(24, I18N.message("24month"));
		cbxRejectTermFrom.addItem(36);
		cbxRejectTermFrom.setItemCaption(36, I18N.message("36month"));
		cbxRejectTermFrom.addItem(48);
		cbxRejectTermFrom.setItemCaption(48, I18N.message("48month"));
		cbxRejectTermFrom.addItem(60);
		cbxRejectTermFrom.setItemCaption(60, I18N.message("60month"));
		
		cbxRejectTermTo = new ComboBox(I18N.message("reject.term.to"));
		cbxRejectTermTo.setWidth("150px");
		cbxRejectTermTo.addItem(12);
		cbxRejectTermTo.setItemCaption(12, I18N.message("12month"));
		cbxRejectTermTo.addItem(24);
		cbxRejectTermTo.setItemCaption(24, I18N.message("24month"));
		cbxRejectTermTo.addItem(36);
		cbxRejectTermTo.setItemCaption(36, I18N.message("36month"));
		cbxRejectTermTo.addItem(48);
		cbxRejectTermTo.setItemCaption(48, I18N.message("48month"));
		cbxRejectTermTo.addItem(60);
		cbxRejectTermTo.setItemCaption(60, I18N.message("60month"));
		
		txtId = new TextField(I18N.message("ID"));
		
		cbxDealer = new DealerComboBox(I18N.message("dealer"), DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.values());
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -1121619816068986468L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(entityService.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		
		cbxRejectReson = new ComboBox(I18N.message("reject.resons"));
		cbxRejectReson.setImmediate(true);
		cbxRejectReson.setWidth("220px");
		cbxRejectReson.setItemCaption(null, I18N.message("all"));
		List<SupportDecision> supportDecisions = DataReference.getInstance().getSupportDecisions(QuotationWkfStatus.REJ);
		for (SupportDecision supportDecision : supportDecisions) {
			cbxRejectReson.addItem(supportDecision);
			cbxRejectReson.setItemCaption(supportDecision, supportDecision.getDescEn());
		}
		
		cbxFieldCheck = new ComboBox(I18N.message("field.check"));
		cbxFieldCheck.setImmediate(true);
		cbxFieldCheck.setItemCaption(true, I18N.message("yes"));
		cbxFieldCheck.addItem(true);
		cbxFieldCheck.setItemCaption(false, I18N.message("no"));
		cbxFieldCheck.addItem(false);
		
		cbxAdvancePayment = new ComboBox(I18N.message("advance.payment"));
		cbxAdvancePayment.setImmediate(true);
		cbxAdvancePayment.setItemCaption(null, I18N.message("all"));
		cbxAdvancePayment.addItem(10.0);
		cbxAdvancePayment.setItemCaption(10.0, I18N.message("10%"));
		cbxAdvancePayment.addItem(20.0);
		cbxAdvancePayment.setItemCaption(20.0, I18N.message("20%"));
		cbxAdvancePayment.addItem(30.0);
		cbxAdvancePayment.setItemCaption(30.0, I18N.message("30%"));
		cbxAdvancePayment.addItem(40.0);
		cbxAdvancePayment.setItemCaption(40.0, I18N.message("40%"));
		cbxAdvancePayment.addItem(50.0);
		cbxAdvancePayment.setItemCaption(50.0, I18N.message("50%"));
		cbxAdvancePayment.addItem(60.0);
		cbxAdvancePayment.setItemCaption(60.0, I18N.message("60%"));
		cbxAdvancePayment.addItem(70.0);
		cbxAdvancePayment.setItemCaption(70.0, I18N.message("70%"));
		
		cbxTerm = new ComboBox(I18N.message("terms"));
		cbxTerm.setImmediate(true);
		cbxTerm.setItemCaption(null, I18N.message("all"));
		cbxTerm.addItem(12);
		cbxTerm.setItemCaption(12, I18N.message("12month"));
		cbxTerm.addItem(24);
		cbxTerm.setItemCaption(24, I18N.message("24month"));
		cbxTerm.addItem(36);
		cbxTerm.setItemCaption(36, I18N.message("36month"));
		cbxTerm.addItem(48);
		cbxTerm.setItemCaption(48, I18N.message("48month"));
		cbxTerm.addItem(60);
		cbxTerm.setItemCaption(60, I18N.message("60month"));
		
		//reset();
		final GridLayout gridLayout = new GridLayout(10, 3);
		gridLayout.setSpacing(true);
		
		FormLayout formLayoutLeft = new FormLayout();
			formLayoutLeft.addComponent(cbxRejectTermFrom);
			formLayoutLeft.addComponent(cbxDealerType);
			formLayoutLeft.addComponent(cbxFieldCheck);
		FormLayout formLayoutMiddle = new FormLayout();
			formLayoutMiddle.addComponent(cbxRejectTermTo);
			formLayoutMiddle.addComponent(cbxDealer);
			formLayoutMiddle.addComponent(cbxAdvancePayment);
		FormLayout formLayoutRight = new FormLayout();
			formLayoutRight.addComponent(txtId);
			formLayoutRight.addComponent(cbxRejectReson);
			formLayoutRight.addComponent(cbxTerm);
		
		int iCol = 0;
		gridLayout.addComponent(formLayoutLeft, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(formLayoutMiddle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(formLayoutRight, iCol++, 0);
	  
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Quotation> getRestrictions() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.REJ));
		if(cbxRejectTermFrom.getValue() != null){
			restrictions.addCriterion(Restrictions.ge("term", cbxRejectTermFrom.getValue()));
		}
		if(cbxRejectTermTo.getValue() != null){
			restrictions.addCriterion(Restrictions.le("term", cbxRejectTermTo.getValue()));
		}
		if(txtId.getValue() != null && !txtId.getValue().isEmpty()){
			long searchID = Long.valueOf(txtId.getValue().replaceAll(",", "").toString());
			restrictions.addCriterion(Restrictions.eq("id", Long.valueOf(searchID)));
		}
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation(DEALER, "indeal", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("indeal.dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER, cbxDealer.getSelectedEntity()));
		}
		if(cbxRejectReson.getValue() != null){
			restrictions.addAssociation("quotationSupportDecisions", "quosudec", JoinType.INNER_JOIN);
			restrictions.addAssociation("quosudec.supportDecision", "sudec", JoinType.INNER_JOIN);
			restrictions.addCriterion("quosudec.supportDecision", (Serializable) cbxRejectReson.getValue());
		}
		if(cbxAdvancePayment.getValue() != null){
			restrictions.addCriterion(Restrictions.eq("advancePaymentPercentage", cbxAdvancePayment.getValue()));
		}
		if(cbxTerm.getValue() != null){
			restrictions.addCriterion(Restrictions.eq("term", cbxTerm.getValue()));
		}
		if(cbxFieldCheck.getValue() != null ){
			restrictions.addCriterion(Restrictions.eq("fieldCheckPerformed", cbxFieldCheck.getValue()));
		}
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbxRejectTermFrom.setValue(null);
		cbxRejectTermTo.setValue(null);
		txtId.setValue("");
		cbxRejectReson.setValue(null);
		cbxFieldCheck.setValue(null);
		cbxAdvancePayment.setValue(null);
		cbxTerm.setValue(null);
	}
}

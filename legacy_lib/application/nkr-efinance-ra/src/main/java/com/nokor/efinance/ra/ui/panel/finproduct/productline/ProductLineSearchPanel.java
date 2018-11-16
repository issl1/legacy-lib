package com.nokor.efinance.ra.ui.panel.finproduct.productline;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.payment.model.EPaymentCondition;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Payment Condition Search
 * @author youhort.ly
 *
 */
public class ProductLineSearchPanel extends AbstractSearchPanel<ProductLine> implements FMEntityField {

    /** */
	private static final long serialVersionUID = 2271725276716326304L;
	
	private TextField txtDesc;
    private TextField txtDescEn;
    private ERefDataComboBox<EProductLineType> cbxProductLineType;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionFin;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionCap;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionIap;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionIma;
    private EntityRefComboBox<EPaymentCondition> cbxPaymentConditionFee;
    
    /**
     * 
     * @param produceLineTablePanel
     */
	public ProductLineSearchPanel(ProductLineTablePanel produceLineTablePanel) {
		super(I18N.message("product.line.search"), produceLineTablePanel);
	}
	
	/**
	 * 
	 * @param caption
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T> getEntityRefComboBox(String caption, BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>(I18N.message(caption));
		comboBox.setWidth("170px");
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		comboBox.addStyleName("mytextfield-caption");
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		return formLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDesc.setValue("");
		txtDescEn.setValue("");
		cbxProductLineType.setSelectedEntity(null);	
		cbxPaymentConditionFin.setSelectedEntity(null);
		cbxPaymentConditionCap.setSelectedEntity(null);
		cbxPaymentConditionIap.setSelectedEntity(null);
		cbxPaymentConditionIma.setSelectedEntity(null);
		cbxPaymentConditionFee.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);	
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 170);		
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 170);	
		cbxProductLineType = new ERefDataComboBox<EProductLineType>(I18N.message("product.line.type"), EProductLineType.class);
		cbxProductLineType.setWidth("170px");
		
		cbxPaymentConditionFin = getEntityRefComboBox("payment.condition.funding", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class));
		cbxPaymentConditionCap = getEntityRefComboBox("payment.condition.capital", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class));
		cbxPaymentConditionIap = getEntityRefComboBox("payment.condition.interest.applicant", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class));
		cbxPaymentConditionIap.addStyleName("mytextfield-caption-19");
		cbxPaymentConditionIma = getEntityRefComboBox("payment.condition.interest", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class));
		cbxPaymentConditionFee = getEntityRefComboBox("payment.condition.fee", 
				new BaseRestrictions<EPaymentCondition>(EPaymentCondition.class));
		
	    FormLayout formLayout = getFormLayout();
	    formLayout.addComponent(cbxProductLineType);
	    formLayout.addComponent(cbxPaymentConditionIap);
	    formLayout.addComponent(txtDescEn);
	    horizontalLayout.addComponent(formLayout);
	    formLayout = getFormLayout();
	    formLayout.addComponent(cbxPaymentConditionIma);
	    formLayout.addComponent(cbxPaymentConditionCap);
	    formLayout.addComponent(txtDesc);
	    horizontalLayout.addComponent(formLayout);
	    formLayout = getFormLayout();
	    formLayout.addComponent(cbxPaymentConditionFin);
	    formLayout.addComponent(cbxPaymentConditionFee);
	    horizontalLayout.addComponent(formLayout);
		return horizontalLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<ProductLine> getRestrictions() {		
		BaseRestrictions<ProductLine> restrictions = new BaseRestrictions<ProductLine>(ProductLine.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (StringUtils.isNotEmpty(txtDesc.getValue())) { 
			criterions.add(Restrictions.ilike(DESC, txtDesc.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxProductLineType.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(PRODUCT_LINE_TYPE, cbxProductLineType.getSelectedEntity()));
		}
		if (cbxPaymentConditionFin.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(PAYMENT_CONDITION_FIN +"."+ ID, cbxPaymentConditionFin.getSelectedEntity().getId()));
		}
		if (cbxPaymentConditionCap.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(PAYMENT_CONDITION_CAP +"."+ ID, cbxPaymentConditionCap.getSelectedEntity().getId()));
		}
		if (cbxPaymentConditionIap.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(PAYMENT_CONDITION_IAP +"."+ ID, cbxPaymentConditionIap.getSelectedEntity().getId()));
		}
		if (cbxPaymentConditionIma.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(PAYMENT_CONDITION_IMA +"."+ ID, cbxPaymentConditionIma.getSelectedEntity().getId()));
		}
		if (cbxPaymentConditionFee.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(PAYMENT_CONDITION_FEE +"."+ ID, cbxPaymentConditionFee.getSelectedEntity().getId()));
		}
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.desc(ID));
		return restrictions;
	}
}

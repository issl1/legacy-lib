package com.nokor.efinance.ra.ui.panel.finproduct.product;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.stock.model.Product;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author youhort.ly
 *
 */
public class ProductSearchPanel extends AbstractSearchPanel<Product> implements FMEntityField {

	private static final long serialVersionUID = -2133808227752046417L;
	
	private CheckBox cbActive;
	private CheckBox cbInactive;
	private TextField txtCode;
	private TextField txtDescEn;
	
	public ProductSearchPanel(ProductTablePanel productTablePanel) {
		super(I18N.message("search"), productTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
	}


	@Override
	protected Component createForm() {
		
		final GridLayout gridLayout = new GridLayout(4, 1);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        cbInactive = new CheckBox(I18N.message("inactive"));
        cbInactive.setValue(false);        
        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
        gridLayout.addComponent(new FormLayout(cbActive), 2, 0);
        gridLayout.addComponent(new FormLayout(cbInactive), 3, 0);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<Product> getRestrictions() {
		
		BaseRestrictions<Product> restrictions = new BaseRestrictions<>(Product.class);
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		if (!cbActive.getValue() && !cbInactive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		} 
		if (cbActive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		}
		if (cbInactive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		}
		
		return restrictions;
	}

}

package com.nokor.efinance.ra.ui.panel.document;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Document Group search
 * @author youhort.ly
 *
 */
public class DocumentGroupSearchPanel extends AbstractSearchPanel<DocumentGroup> implements FMEntityField {

	private static final long serialVersionUID = 9136565522330994370L;
	
	private CheckBox cbActive;
	private CheckBox cbInactive;
	private TextField txtCode;
	private TextField txtDescEn;
	
	public DocumentGroupSearchPanel(DocumentGroupTablePanel documentGroupTablePanel) {
		super(I18N.message("document.group.search"), documentGroupTablePanel);
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
	public BaseRestrictions<DocumentGroup> getRestrictions() {		
		BaseRestrictions<DocumentGroup> restrictions = new BaseRestrictions<DocumentGroup>(DocumentGroup.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			criterions.add(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
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
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}

}

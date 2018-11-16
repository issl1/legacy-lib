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

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Document search
 * @author youhort.ly
 *
 */
public class DocumentSearchPanel extends AbstractSearchPanel<Document> implements FMEntityField {

	private static final long serialVersionUID = 5489374367808132695L;

	private CheckBox cbActive;
	private CheckBox cbInactive;
	private TextField txtCode;
	private TextField txtDescEn;
	private ERefDataComboBox<EApplicantType> cbxApplicantType;
	private EntityRefComboBox<DocumentGroup> cbxDocumentGroup;
	
	public DocumentSearchPanel(DocumentTablePanel documentTablePanel) {
		super(I18N.message("document.search"), documentTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
	}


	@Override
	protected Component createForm() {
		
		final GridLayout gridLayout = new GridLayout(7, 2);
		
		txtCode = ComponentFactory.getTextField(false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField(false, 60, 200);
		
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        cbInactive = new CheckBox(I18N.message("inactive"));
        cbInactive.setValue(false);
        
        cbxApplicantType = new ERefDataComboBox<EApplicantType>(EApplicantType.class);
        
        cbxDocumentGroup = new EntityRefComboBox<DocumentGroup>();
        cbxDocumentGroup.setRestrictions(new BaseRestrictions<DocumentGroup>(DocumentGroup.class));
        cbxDocumentGroup.renderer();
        
        int iCol = 0;
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new Label(I18N.message("code")), iCol++, 0);
        gridLayout.addComponent(txtCode, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("desc.en")), iCol++, 0);
        gridLayout.addComponent(txtDescEn, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new HorizontalLayout(cbActive, cbInactive), iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("applicant.type")), iCol++, 1);
        gridLayout.addComponent(cbxApplicantType, iCol++, 1);
        
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("document.group")), iCol++, 1);
        gridLayout.addComponent(cbxDocumentGroup, iCol++, 1);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<Document> getRestrictions() {		
		BaseRestrictions<Document> restrictions = new BaseRestrictions<Document>(Document.class);		
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
		
		if (cbxApplicantType.getSelectedEntity() != null) {
			criterions.add(Restrictions.eq(APPLICANT_TYPE, cbxApplicantType.getSelectedEntity()));
		}
		
		if (cbxDocumentGroup.getSelectedEntity() != null) {
			criterions.add(Restrictions.eq("documentGroup.id", cbxDocumentGroup.getSelectedEntity().getId()));
		}
		
		restrictions.setCriterions(criterions);	
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}

}

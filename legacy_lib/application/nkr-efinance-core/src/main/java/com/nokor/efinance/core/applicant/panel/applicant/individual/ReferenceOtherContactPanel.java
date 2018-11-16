package com.nokor.efinance.core.applicant.panel.applicant.individual;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ReferenceOtherContactPanel extends AbstractControlPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -858242160910149803L;
	
	//private Individual individual;
	
	private IndividualReferenceInfo individualReferenceInfo;
	private Button btnAdd;
	
	private VerticalLayout otherContactLayout;
	
	public ReferenceOtherContactPanel() {
		setMargin(true);
		setSpacing(true);
		otherContactLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		addComponent(otherContactLayout);
		addComponent(btnAdd);
	}
	
	/**
	 * 
	 */
	public void assignValue(IndividualReferenceInfo individualReferenceInfo) {
		this.individualReferenceInfo = individualReferenceInfo;
		otherContactLayout.removeAllComponents();
		btnAdd.setEnabled(true);
		if (individualReferenceInfo.getId() != null) {
			List<IndividualReferenceContactInfo> individualReferenceContactInfos = getIndividualContactInfos(individualReferenceInfo);
			if (individualReferenceContactInfos != null && !individualReferenceContactInfos.isEmpty()) {
				for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
					ContactInfo contactInfo = individualReferenceContactInfo.getContactInfo();
					otherContactLayout.addComponent(getOtherContactInfoLayout(contactInfo));
				}
			} else {
				ContactInfo contactInfo = ContactInfo.createInstance();
				otherContactLayout.addComponent(getOtherContactInfoLayout(contactInfo));
			}
		} else {
			ContactInfo contactInfo = ContactInfo.createInstance();
			otherContactLayout.addComponent(getOtherContactInfoLayout(contactInfo));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout getOtherContactInfoLayout(ContactInfo contactInfo) {
		Label lblTypeContactInfo = ComponentLayoutFactory.getLabelCaptionRequired("type");
		Label lblValue = ComponentLayoutFactory.getLabelCaption("value");
		TextField txtValue = ComponentFactory.getTextField(60, 200);
		ERefDataComboBox<ETypeContactInfo> cbxTypeContactInfo = new ERefDataComboBox<>(getOtherContactValues());
		cbxTypeContactInfo.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 2899998111329289920L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				txtValue.setValue("");
			}
		});
		
		Button btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -3172953851998484406L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (individualReferenceInfo.getId() != null) {
					if (btnSave.getCaption().equals(I18N.message("save"))) {
						if (cbxTypeContactInfo.getSelectedEntity() != null) {
							btnSave.setCaption(I18N.message("edit"));
							btnSave.setIcon(FontAwesome.EDIT);
							btnAdd.setEnabled(true);
							
							cbxTypeContactInfo.setEnabled(false);
							txtValue.setEnabled(false);
							
							IndividualReferenceContactInfo individualReferenceContactInfo = IndividualReferenceContactInfo.createInstance();
							individualReferenceContactInfo.setIndividualReferenceInfo(individualReferenceInfo);
							individualReferenceContactInfo.setContactInfo(contactInfo);
							
							contactInfo.setTypeInfo(cbxTypeContactInfo.getSelectedEntity());
							contactInfo.setValue(txtValue.getValue());
							if (contactInfo.getId() != null) {
								ENTITY_SRV.saveOrUpdate(contactInfo);
							} else {
								ENTITY_SRV.saveOrUpdate(contactInfo);
								ENTITY_SRV.saveOrUpdate(individualReferenceContactInfo);
							}
							ComponentLayoutFactory.displaySuccessfullyMsg();
							assignValue(individualReferenceInfo);
						} else {
							ComponentLayoutFactory.displayErrorMsg("type.contact.info.is.required");
						}
					} else {
						btnSave.setCaption(I18N.message("save"));
						btnSave.setIcon(FontAwesome.SAVE);
						btnAdd.setEnabled(false);
						
						cbxTypeContactInfo.setEnabled(true);
						txtValue.setEnabled(true);
					}
				} else {
					ComponentLayoutFactory.displayErrorMsg("please.save.reference.before.save.contact.info");
				}
			}
		});
		
		if (contactInfo.getId() != null) {
			
			btnSave.setCaption(I18N.message("edit"));
			btnSave.setIcon(FontAwesome.EDIT);
			
			cbxTypeContactInfo.setSelectedEntity(contactInfo.getTypeInfo());
			txtValue.setValue(getDefaultString(contactInfo.getValue()));
			
			cbxTypeContactInfo.setEnabled(false);
			txtValue.setEnabled(false);
		} else btnAdd.setEnabled(false);
		
		HorizontalLayout otherContactLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		otherContactLayout.addComponent(lblTypeContactInfo);
		otherContactLayout.addComponent(cbxTypeContactInfo);
		otherContactLayout.addComponent(lblValue);
		otherContactLayout.addComponent(txtValue);
		otherContactLayout.addComponent(btnSave);
		return otherContactLayout;
	}
	
	/**
	 * getIndividualContactInfos 
	 * @param individual
	 * @return
	 */
	private List<IndividualReferenceContactInfo> getIndividualContactInfos(IndividualReferenceInfo individualReferenceInfo) {
		BaseRestrictions<IndividualReferenceContactInfo> restrictions = new BaseRestrictions<>(IndividualReferenceContactInfo.class);
		restrictions.addAssociation("contactInfo", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("individualReferenceInfo", individualReferenceInfo));
		restrictions.addCriterion(Restrictions.and(Restrictions.ne("con.typeInfo", ETypeContactInfo.LANDLINE), Restrictions.ne("con.typeInfo", ETypeContactInfo.MOBILE)));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ETypeContactInfo> getOtherContactValues() {
		List<ETypeContactInfo> typeContactInfos	 = new ArrayList<ETypeContactInfo>();
		typeContactInfos.add(ETypeContactInfo.FAX);
		typeContactInfos.add(ETypeContactInfo.EMAIL);
		typeContactInfos.add(ETypeContactInfo.SKYPE);
		typeContactInfos.add(ETypeContactInfo.GMAIL);
		typeContactInfos.add(ETypeContactInfo.YAHOO);
		typeContactInfos.add(ETypeContactInfo.LINKEDIN);
		typeContactInfos.add(ETypeContactInfo.TWITTER);
		typeContactInfos.add(ETypeContactInfo.FACEBOOK);
		
		return typeContactInfos;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			ContactInfo contactInfo = ContactInfo.createInstance();
			otherContactLayout.addComponent(getOtherContactInfoLayout(contactInfo));
			btnAdd.setEnabled(false);
		}
		
	}

}

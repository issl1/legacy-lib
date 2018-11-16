package com.nokor.efinance.core.contact.panel;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerContactInfo;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
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
public class UserOtherContactPanel extends AbstractControlPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -858242160910149803L;
	
	private Individual individual;
	private Dealer dealer;
	private Button btnAdd;
	
	private VerticalLayout otherContactLayout;
	
	public UserOtherContactPanel() {
		setMargin(new MarginInfo(true, true, false, true));
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
	public void assignValue() {
		otherContactLayout.removeAllComponents();
		btnAdd.setEnabled(true);
		List<IndividualContactInfo> individualContactInfos = null;
		List<DealerContactInfo> dealerContactInfos = null;
		if (individual != null) {
			individualContactInfos = getIndividualContactInfos(individual);
		} else if (dealer != null) {
			dealerContactInfos = getDealerContactInfos(dealer);
		}
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			for (IndividualContactInfo individualContactInfo : individualContactInfos) {
				ContactInfo contactInfo = individualContactInfo.getContactInfo();
				otherContactLayout.addComponent(getOtherContactInfoLayout(contactInfo));
			}
		} else if (dealerContactInfos != null && !dealerContactInfos.isEmpty()) {
			for (DealerContactInfo dealerContactInfo : dealerContactInfos) {
				ContactInfo contactInfo = dealerContactInfo.getContactInfo();
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
		cbxTypeContactInfo.setNullSelectionAllowed(false);
		cbxTypeContactInfo.setSelectedEntity(ETypeContactInfo.FACEBOOK);
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
				if (btnSave.getCaption().equals(I18N.message("save"))) {
					if (cbxTypeContactInfo.getSelectedEntity() != null) {
						btnSave.setCaption(I18N.message("edit"));
						btnSave.setIcon(FontAwesome.EDIT);
						btnAdd.setEnabled(true);
						
						cbxTypeContactInfo.setEnabled(false);
						txtValue.setEnabled(false);
						
						IndividualContactInfo individualContactInfo = null;
						DealerContactInfo dealerContactInfo = null;
						if (contactInfo.getId() == null) {
							if (individual != null) {
								individualContactInfo = IndividualContactInfo.createInstance();
								individualContactInfo.setIndividual(individual);
								individualContactInfo.setContactInfo(contactInfo);
							} else if (dealer != null) {
								dealerContactInfo = DealerContactInfo.createInstance();
								dealerContactInfo.setDealer(dealer);
								dealerContactInfo.setContactInfo(contactInfo);
							}
						}
						contactInfo.setTypeInfo(cbxTypeContactInfo.getSelectedEntity());
						contactInfo.setValue(txtValue.getValue());
						
						try {
							if (contactInfo.getId() == null) {
								ENTITY_SRV.saveOrUpdate(contactInfo);
								if (individualContactInfo != null) {
									ENTITY_SRV.saveOrUpdate(individualContactInfo);
								} else if (dealerContactInfo != null) {
									ENTITY_SRV.saveOrUpdate(dealerContactInfo);
								}
							} else {
								ENTITY_SRV.update(contactInfo);
							}
							ComponentLayoutFactory.displaySuccessfullyMsg();
							assignValue();
						} catch (Exception e) {
							e.printStackTrace();
						}
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
	private List<IndividualContactInfo> getIndividualContactInfos(Individual individual) {
		BaseRestrictions<IndividualContactInfo> restrictions = new BaseRestrictions<>(IndividualContactInfo.class);
		restrictions.addAssociation("contactInfo", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("individual", individual));
		restrictions.addCriterion(Restrictions.and(Restrictions.ne("con.typeInfo", ETypeContactInfo.LANDLINE), Restrictions.ne("con.typeInfo", ETypeContactInfo.MOBILE)));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 *  
	 * @param dealer
	 * @return
	 */
	private List<DealerContactInfo> getDealerContactInfos(Dealer dealer) {
		BaseRestrictions<DealerContactInfo> restrictions = new BaseRestrictions<>(DealerContactInfo.class);
		restrictions.addAssociation("contactInfo", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("dealer", dealer));
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
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			ContactInfo contactInfo = ContactInfo.createInstance();
			otherContactLayout.addComponent(getOtherContactInfoLayout(contactInfo));
			btnAdd.setEnabled(false);
		}
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

}
